#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

BASE="http://localhost:8080"
REPORT_MD="$SCRIPT_DIR/demo_report.md"
REPORT_PDF="$SCRIPT_DIR/demo_report.pdf"
STEP=0
SUFFIX="$(date +%s)"
SUFFIX_SHORT="$(printf "%06d" $((SUFFIX % 1000000)))"
FIXED_COL_WIDTHS_MM="44,76,76,90,90,90"
EXTRA_RESULT_CENTRAL=""
EXTRA_RESULT_BRANCH1=""
EXTRA_RESULT_BRANCH2=""

req() {
  local method="$1"; shift
  local url="$1"; shift
  local body="${1-}"
  if [ -n "$body" ]; then
    curl -s -w "\n%{http_code}" -X "$method" -H "Content-Type: application/json" -d "$body" "$url"
  else
    curl -s -w "\n%{http_code}" -X "$method" "$url"
  fi
}

split_resp() {
  local resp="$1"
  body="${resp%$'\n'*}"
  code="${resp##*$'\n'}"
}

format_json() {
  local json="$1"
  JSON_STR="$json" python3 - <<PY
import json,os
s=os.environ["JSON_STR"]
try:
    obj=json.loads(s)
    print(json.dumps(obj, ensure_ascii=False, indent=2))
except Exception:
    print(s)
PY
}

json_field() {
  local json="$1"
  local field="$2"
  JSON_STR="$json" python3 - <<PY
import json,os
obj=json.loads(os.environ["JSON_STR"])
print(obj["$field"])
PY
}

json_list_find_by_name() {
  local json="$1"
  local name="$2"
  JSON_STR="$json" python3 - <<PY
import json,os
arr=json.loads(os.environ["JSON_STR"])
for it in arr:
    if it.get("name")=="$name":
        print(it.get("branchId"))
        break
PY
}

json_list_contains_id() {
  local json="$1"
  local field="$2"
  local val="$3"
  JSON_STR="$json" python3 - <<PY
import json,os
arr=json.loads(os.environ["JSON_STR"])
val=int("$val")
print("да" if any(it.get("$field")==val for it in arr) else "нет")
PY
}

psql_central() {
  docker exec postgres_central psql -U central_user -d central_db -t -A -c "$1"
}

psql_branch1() {
  docker exec postgres_branch1 psql -U branch_user -d branch1_db -t -A -c "$1"
}

psql_branch2() {
  docker exec postgres_branch2 psql -U branch_user -d branch2_db -t -A -c "$1"
}

psql_db() {
  local db="$1"; shift
  local sql="$1"
  case "$db" in
    central) psql_central "$sql" ;;
    branch1) psql_branch1 "$sql" ;;
    branch2) psql_branch2 "$sql" ;;
    *) echo "Unknown db: $db" >&2; exit 1 ;;
  esac
}

dump_table_json() {
  local db="$1"
  local table="$2"
  local order_by="$3"
  local out_file="$4"
  local sql="select coalesce(json_agg(t), '[]'::json) from (select * from $table order by $order_by) t;"
  psql_db "$db" "$sql" > "$out_file"
}

diff_table_text() {
  local table_label="$1"
  local before_file="$2"
  local after_file="$3"
  local pk_csv="$4"

  BEFORE_JSON="$(cat "$before_file")" AFTER_JSON="$(cat "$after_file")" PKS="$pk_csv" TABLE_LABEL="$table_label" python3 - <<'PY'
import json,os
before = json.loads(os.environ["BEFORE_JSON"])
after = json.loads(os.environ["AFTER_JSON"])
keys = [k.strip() for k in os.environ["PKS"].split(',') if k.strip()]

def key(row):
    return tuple(row.get(k) for k in keys)

b = {key(r): r for r in before}
a = {key(r): r for r in after}

added = [a[k] for k in a.keys() - b.keys()]
removed = [b[k] for k in b.keys() - a.keys()]
changed = [{"before": b[k], "after": a[k]} for k in a.keys() & b.keys() if b[k] != a[k]]

if not added and not removed and not changed:
    print("")
else:
    print(f"Таблица: {os.environ.get('TABLE_LABEL', '')}")
    def dump_block(title, obj):
        if not obj:
            print(f"{title}: нет")
            return
        print(f"{title}:")
        print(json.dumps(obj, ensure_ascii=False, indent=2))
    dump_block("Добавлено", added)
    dump_block("Удалено", removed)
    dump_block("Изменено", changed)
PY
}

html_cell() {
  local text="$1"
  if [ -z "$text" ]; then
    echo "—"
    return
  fi
  TEXT="$text" python3 - <<'PY'
import os,html
text=os.environ["TEXT"]
lines=text.splitlines() or [""]
out_lines=[]
for line in lines:
    # preserve leading spaces
    leading=len(line)-len(line.lstrip(' '))
    rest=line.lstrip(' ')
    esc=html.escape(rest)
    if leading > 0:
        esc = "&nbsp;" * leading + esc
    out_lines.append(esc)
res = "<br>".join(out_lines)
print(res)
PY
}

append_row() {
  local intent="$1"
  local request="$2"
  local response="$3"
  local result_central="$4"
  local result_branch1="$5"
  local result_branch2="$6"

  local c1 c2 c3 c4 c5 c6
  c1=$(html_cell "$intent")
  c2=$(html_cell "$request")
  c3=$(html_cell "$response")
  c4=$(html_cell "$result_central")
  c5=$(html_cell "$result_branch1")
  c6=$(html_cell "$result_branch2")

  cat >> "$REPORT_MD" <<HTML
<tr>
  <td><div class="cell">$c1</div></td>
  <td><div class="cell block">$c2</div></td>
  <td><div class="cell block">$c3</div></td>
  <td><div class="cell block">$c4</div></td>
  <td><div class="cell block">$c5</div></td>
  <td><div class="cell block">$c6</div></td>
</tr>
HTML
}

write_header() {
  local colgroup=""
  local table_width_mm
  table_width_mm=$(awk -F',' '{sum=0; for(i=1;i<=NF;i++) sum+=$i; print sum}' <<<"$FIXED_COL_WIDTHS_MM")
  IFS=',' read -ra cols <<< "$FIXED_COL_WIDTHS_MM"
  for w in "${cols[@]}"; do
    w="$(echo "$w" | xargs)"
    colgroup+="    <col style=\"width: ${w}mm;\">"$'\n'
  done

  : > "$REPORT_MD"
  cat >> "$REPORT_MD" <<HTML
<h1>Демо-отчет по API</h1>
<p class="meta">Дата: $(date -Iseconds)</p>
<style>
  @page { margin: 8mm; }
  body { font-family: "DejaVu Serif", "Times New Roman", serif; font-size: 10pt; color: #111; }
  h1 { margin-bottom: 4px; }
  .meta { margin-top: 0; color: #444; }
  table { width: ${table_width_mm}mm; border-collapse: collapse; margin-top: 12px; table-layout: fixed; }
  th, td { border: 1px solid #444; padding: 4px; vertical-align: top; }
  th { background: #f0f0f0; }
  .cell { white-space: pre-wrap; word-wrap: break-word; overflow-wrap: anywhere; word-break: break-word; hyphens: auto; font-family: "DejaVu Sans Mono", "Courier New", monospace; font-size: 9pt; line-height: 1.2; }
  .block { background: #f4f4f4; border-radius: 3px; padding: 4px; }
</style>
<table>
  <colgroup>
$colgroup  </colgroup>
  <thead>
    <tr>
      <th rowspan="2">Намерение</th>
      <th rowspan="2">Выполнение запроса</th>
      <th rowspan="2">Получение ответа</th>
      <th colspan="3">Результат</th>
    </tr>
    <tr>
      <th>central</th>
      <th>branch1</th>
      <th>branch2</th>
    </tr>
  </thead>
  <tbody>
HTML
}

write_footer() {
  cat >> "$REPORT_MD" <<HTML
  </tbody>
</table>
HTML
}

generate_pdf() {
  local tmp_html="${REPORT_MD%.md}.tmp.html"
  rm -f "$tmp_html" 2>/dev/null || true
  {
    echo '<!doctype html><html lang="ru"><head><meta charset="utf-8"></head><body>'
    cat "$REPORT_MD"
    echo '</body></html>'
  } > "$tmp_html"

  if command -v wkhtmltopdf >/dev/null 2>&1; then
    wkhtmltopdf --enable-local-file-access --page-size A3 --orientation Landscape "$tmp_html" "$REPORT_PDF"
  else
    docker run --rm -v "$SCRIPT_DIR":/data icalialabs/wkhtmltopdf:latest --enable-local-file-access --page-size A3 --orientation Landscape "/data/$(basename "$tmp_html")" "/data/$(basename "$REPORT_PDF")"
  fi

  rm -f "$tmp_html" 2>/dev/null || true
}

run_step() {
  local hook=""
  local display_path=""
  while [ "${1-}" = "--hook" ] || [ "${1-}" = "--display-path" ]; do
    case "$1" in
      --hook)
        hook="$2"
        shift 2
        ;;
      --display-path)
        display_path="$2"
        shift 2
        ;;
    esac
  done
  local intent="$1"; shift
  local method="$1"; shift
  local url="$1"; shift
  local body_in="${1-}"; shift || true
  local items=("$@")

  STEP=$((STEP+1))
  local step_title="Шаг $(printf "%02d" "$STEP")."$'\n'"$intent"

  local path="${display_path:-${url#"$BASE"}}"
  local request_text="$method $path"
  if [ -n "$body_in" ]; then
    request_text+=$'\n'"$(format_json "$body_in")"
  fi

  local item_db=()
  local item_table=()
  local item_pk=()
  local before_file=()

  if [ "${#items[@]}" -gt 0 ]; then
    for i in "${!items[@]}"; do
      IFS='|' read -r db table pk <<< "${items[$i]}"
      item_db[$i]="$db"
      item_table[$i]="$table"
      item_pk[$i]="$pk"
      before_file[$i]="/tmp/demo_${STEP}_${db}_${table}_before.json"
      dump_table_json "$db" "$table" "$pk" "${before_file[$i]}"
    done
  fi

  local resp
  resp=$(req "$method" "$url" "$body_in")
  split_resp "$resp"

  local response_text="HTTP $code"
  if [ -n "$body" ]; then
    response_text+=$'\n'"$(format_json "$body")"
  fi

  local result_central=""
  local result_branch1=""
  local result_branch2=""

  if [ "${#items[@]}" -eq 0 ]; then
    result_central="—"
    result_branch1="—"
    result_branch2="—"
  else
    for i in "${!items[@]}"; do
      local db="${item_db[$i]}"
      local table="${item_table[$i]}"
      local pk="${item_pk[$i]}"
      local after_file="/tmp/demo_${STEP}_${db}_${table}_after.json"
      dump_table_json "$db" "$table" "$pk" "$after_file"
      local diff
      diff=$(diff_table_text "$db.$table" "${before_file[$i]}" "$after_file" "$pk")
      if [ -z "$diff" ]; then
        continue
      fi

      case "$db" in
        central)
          if [ -n "$result_central" ]; then result_central+=$'\n\n'; fi
          result_central+="$diff"
          ;;
        branch1)
          if [ -n "$result_branch1" ]; then result_branch1+=$'\n\n'; fi
          result_branch1+="$diff"
          ;;
        branch2)
          if [ -n "$result_branch2" ]; then result_branch2+=$'\n\n'; fi
          result_branch2+="$diff"
          ;;
      esac
    done

    if [ -z "$result_central" ]; then result_central="—"; fi
    if [ -z "$result_branch1" ]; then result_branch1="—"; fi
    if [ -z "$result_branch2" ]; then result_branch2="—"; fi
  fi

  if [ -n "$hook" ]; then
    "$hook"
  fi

  if [ -n "$EXTRA_RESULT_CENTRAL" ]; then
    if [ "$result_central" = "—" ]; then
      result_central="$EXTRA_RESULT_CENTRAL"
    else
      if [ -n "$result_central" ]; then result_central+=$'\n\n'; fi
      result_central+="$EXTRA_RESULT_CENTRAL"
    fi
  fi
  if [ -n "$EXTRA_RESULT_BRANCH1" ]; then
    if [ "$result_branch1" = "—" ]; then
      result_branch1="$EXTRA_RESULT_BRANCH1"
    else
      if [ -n "$result_branch1" ]; then result_branch1+=$'\n\n'; fi
      result_branch1+="$EXTRA_RESULT_BRANCH1"
    fi
  fi
  if [ -n "$EXTRA_RESULT_BRANCH2" ]; then
    if [ "$result_branch2" = "—" ]; then
      result_branch2="$EXTRA_RESULT_BRANCH2"
    else
      if [ -n "$result_branch2" ]; then result_branch2+=$'\n\n'; fi
      result_branch2+="$EXTRA_RESULT_BRANCH2"
    fi
  fi

  EXTRA_RESULT_CENTRAL=""
  EXTRA_RESULT_BRANCH1=""
  EXTRA_RESULT_BRANCH2=""

  append_row "$step_title" "$request_text" "$response_text" "$result_central" "$result_branch1" "$result_branch2"
}

isolation_branch1_hook() {
  local contains
  contains=$(json_list_contains_id "$body" guestId "$branch2_guest_id")
  EXTRA_RESULT_BRANCH1="Гость из филиала №2 в филиале №1: $contains"
}

isolation_branch2_hook() {
  local contains
  contains=$(json_list_contains_id "$body" guestId "$branch2_guest_id")
  EXTRA_RESULT_BRANCH2="Гость из филиала №2 в филиале №2: $contains"
}

initial_state_row() {
  local central_text
  central_text="central.department count: $(psql_central "select count(*) from department;")"
  central_text+=$'\n'"central.staff_central count: $(psql_central "select count(*) from staff_central;")"
  central_text+=$'\n'"central.supplier count: $(psql_central "select count(*) from supplier;")"
  central_text+=$'\n'"central.supplier_branch count: $(psql_central "select count(*) from supplier_branch;")"

  local branch1_text
  branch1_text="branch1.guest count: $(psql_branch1 "select count(*) from guest;")"
  branch1_text+=$'\n'"branch1.room count: $(psql_branch1 "select count(*) from room;")"
  branch1_text+=$'\n'"branch1.reservation count: $(psql_branch1 "select count(*) from reservation;")"
  branch1_text+=$'\n'"branch1.staff count: $(psql_branch1 "select count(*) from staff;")"
  branch1_text+=$'\n'"branch1.reservation_staff count: $(psql_branch1 "select count(*) from reservation_staff;")"

  local branch2_text
  branch2_text="branch2.guest count: $(psql_branch2 "select count(*) from guest;")"
  branch2_text+=$'\n'"branch2.room count: $(psql_branch2 "select count(*) from room;")"
  branch2_text+=$'\n'"branch2.reservation count: $(psql_branch2 "select count(*) from reservation;")"
  branch2_text+=$'\n'"branch2.staff count: $(psql_branch2 "select count(*) from staff;")"
  branch2_text+=$'\n'"branch2.reservation_staff count: $(psql_branch2 "select count(*) from reservation_staff;")"

  append_row "Изначальное состояние" "—" "—" "$central_text" "$branch1_text" "$branch2_text"
}

# ---------- Run ----------
write_header
initial_state_row

department_name="Отдел размещения $SUFFIX_SHORT"
department_name_upd="Отдел размещения $SUFFIX_SHORT (обновлено)"
central_staff_name="Иван Петров $SUFFIX_SHORT"
central_staff_name_upd="Иван Петров $SUFFIX_SHORT (обновлено)"
supplier_name="Снабжение $SUFFIX_SHORT"
supplier_name_upd="Снабжение $SUFFIX_SHORT (обновлено)"
departments_search_name="Отдел"
departments_search_encoded="$(python3 - <<PY
import urllib.parse
print(urllib.parse.quote("$departments_search_name"))
PY
)"

guest_name="Сергей Смирнов $SUFFIX_SHORT"
guest_phone_1="+7999${SUFFIX_SHORT}01"
guest_phone_2="+7999${SUFFIX_SHORT}02"
branch_staff_name="Ольга Громова $SUFFIX_SHORT"
branch2_guest_name="Мария Орлова $SUFFIX_SHORT"
branch2_guest_phone="+7999${SUFFIX_SHORT}03"
room_number="$((200 + (SUFFIX % 50)))"

run_step "Центр: список филиалов" GET "$BASE/api/v1/central/branches"
branch1_id=$(json_list_find_by_name "$body" "branch1")
branch2_id=$(json_list_find_by_name "$body" "branch2")

run_step "Центр: создание отдела" POST "$BASE/api/v1/central/departments" "{\"name\":\"$department_name\"}" "central|department|department_id"
department_id=$(json_field "$body" departmentId)

run_step "Центр: обновление отдела" PUT "$BASE/api/v1/central/departments/$department_id" "{\"name\":\"$department_name_upd\"}" "central|department|department_id"

run_step --display-path "/api/v1/central/departments?name=$departments_search_name" "Центр: поиск отделов" GET "$BASE/api/v1/central/departments?name=$departments_search_encoded"

run_step "Центр: создание сотрудника" POST "$BASE/api/v1/central/staff" "{\"name\":\"$central_staff_name\",\"departmentId\":$department_id}" "central|staff_central|staff_id"
staff_id=$(json_field "$body" staffId)

run_step "Центр: обновление сотрудника" PUT "$BASE/api/v1/central/staff/$staff_id" "{\"name\":\"$central_staff_name_upd\",\"departmentId\":$department_id}" "central|staff_central|staff_id"

run_step "Центр: список сотрудников по отделу" GET "$BASE/api/v1/central/staff?departmentId=$department_id"

run_step "Центр: создание поставщика" POST "$BASE/api/v1/central/suppliers" "{\"companyName\":\"$supplier_name\"}" "central|supplier|supplier_id"
supplier_id=$(json_field "$body" supplierId)

run_step "Центр: обновление поставщика" PUT "$BASE/api/v1/central/suppliers/$supplier_id" "{\"companyName\":\"$supplier_name_upd\"}" "central|supplier|supplier_id"

run_step "Центр: привязка поставщика к филиалу №1" POST "$BASE/api/v1/central/suppliers/$supplier_id/branches/$branch1_id" "" "central|supplier_branch|supplier_id,branch_id"

run_step "Центр: список филиалов поставщика" GET "$BASE/api/v1/central/suppliers/$supplier_id/branches"

run_step "Центр: отвязка поставщика от филиала №1" DELETE "$BASE/api/v1/central/suppliers/$supplier_id/branches/$branch1_id" "" "central|supplier_branch|supplier_id,branch_id"

run_step "Центр: удаление поставщика" DELETE "$BASE/api/v1/central/suppliers/$supplier_id" "" "central|supplier|supplier_id"

run_step "Центр: удаление отсутствующего поставщика (негативный)" DELETE "$BASE/api/v1/central/suppliers/999999" "" "central|supplier|supplier_id"

run_step "Центр: удаление сотрудника" DELETE "$BASE/api/v1/central/staff/$staff_id" "" "central|staff_central|staff_id"

run_step "Центр: удаление отдела" DELETE "$BASE/api/v1/central/departments/$department_id" "" "central|department|department_id"

run_step "Центр: обновление отсутствующего отдела (негативный)" PUT "$BASE/api/v1/central/departments/999999" '{"name":"Отсутствующий отдел"}' "central|department|department_id"

run_step "Центр: получение отсутствующего сотрудника (негативный)" GET "$BASE/api/v1/central/staff/999999"

run_step "Филиал №1: создание гостя" POST "$BASE/api/v1/branches/branch1/guests" "{\"fullName\":\"$guest_name\",\"phone\":\"$guest_phone_1\"}" "branch1|guest|guest_id"
guest_id=$(json_field "$body" guestId)

run_step "Филиал №1: обновление гостя" PUT "$BASE/api/v1/branches/branch1/guests/$guest_id" "{\"fullName\":\"$guest_name\",\"phone\":\"$guest_phone_2\"}" "branch1|guest|guest_id"

run_step "Филиал №1: создание номера" POST "$BASE/api/v1/branches/branch1/rooms" "{\"number\":\"$room_number\",\"type\":\"standard\",\"price\":3200}" "branch1|room|room_id"
room_id=$(json_field "$body" roomId)

run_step "Филиал №1: обновление номера" PUT "$BASE/api/v1/branches/branch1/rooms/$room_id" "{\"number\":\"$room_number\",\"type\":\"standard\",\"price\":3500}" "branch1|room|room_id"

run_step "Филиал №1: бронь с отсутствующим гостем (негативный)" POST "$BASE/api/v1/branches/branch1/reservations" "{\"guestId\":999999,\"roomId\":$room_id,\"checkIn\":\"2026-03-10\",\"checkOut\":\"2026-03-12\"}" "branch1|reservation|reservation_id"

run_step "Филиал №1: создание бронирования" POST "$BASE/api/v1/branches/branch1/reservations" "{\"guestId\":$guest_id,\"roomId\":$room_id,\"checkIn\":\"2026-03-10\",\"checkOut\":\"2026-03-12\"}" "branch1|reservation|reservation_id"
reservation_id=$(json_field "$body" reservationId)

run_step "Филиал №1: поиск бронирований по датам" GET "$BASE/api/v1/branches/branch1/reservations?from=2026-03-01&to=2026-03-31"

run_step "Филиал №1: создание сотрудника" POST "$BASE/api/v1/branches/branch1/staff" "{\"name\":\"$branch_staff_name\",\"role\":\"receptionist\"}" "branch1|staff|staff_id"
branch_staff_id=$(json_field "$body" staffId)

run_step "Филиал №1: привязка бронирования к сотруднику" POST "$BASE/api/v1/branches/branch1/reservations/$reservation_id/staff/$branch_staff_id" "" "branch1|reservation_staff|reservation_id,staff_id"

run_step "Филиал №1: список сотрудников бронирования" GET "$BASE/api/v1/branches/branch1/reservations/$reservation_id/staff"

run_step "Филиал №1: отвязка сотрудника от бронирования" DELETE "$BASE/api/v1/branches/branch1/reservations/$reservation_id/staff/$branch_staff_id" "" "branch1|reservation_staff|reservation_id,staff_id"

run_step "Филиал №1: удаление бронирования" DELETE "$BASE/api/v1/branches/branch1/reservations/$reservation_id" "" "branch1|reservation|reservation_id"

run_step "Филиал №1: удаление номера" DELETE "$BASE/api/v1/branches/branch1/rooms/$room_id" "" "branch1|room|room_id"

run_step "Филиал №1: получение отсутствующего номера (негативный)" GET "$BASE/api/v1/branches/branch1/rooms/$room_id"

run_step "Филиал №1: удаление гостя" DELETE "$BASE/api/v1/branches/branch1/guests/$guest_id" "" "branch1|guest|guest_id"

run_step "Филиал №1: получение отсутствующего гостя (негативный)" GET "$BASE/api/v1/branches/branch1/guests/999999"

run_step "Филиал №1: удаление сотрудника" DELETE "$BASE/api/v1/branches/branch1/staff/$branch_staff_id" "" "branch1|staff|staff_id"

run_step "Филиал: неверный код филиала (негативный)" GET "$BASE/api/v1/branches/branchx/guests"

run_step "Филиал №1: неверная цена номера (негативный)" POST "$BASE/api/v1/branches/branch1/rooms" '{"number":"666","type":"standard","price":-1}' "branch1|room|room_id"

run_step "Филиал №1: неверные даты бронирования (негативный)" POST "$BASE/api/v1/branches/branch1/reservations" '{"guestId":1,"roomId":1,"checkIn":"2026-04-10","checkOut":"2026-04-05"}' "branch1|reservation|reservation_id"

run_step "Филиал №2: создание гостя" POST "$BASE/api/v1/branches/branch2/guests" "{\"fullName\":\"$branch2_guest_name\",\"phone\":\"$branch2_guest_phone\"}" "branch2|guest|guest_id"
branch2_guest_id=$(json_field "$body" guestId)

run_step --hook isolation_branch1_hook "Филиал №2: изоляция — гость не виден в филиале №1" GET "$BASE/api/v1/branches/branch1/guests"

run_step --hook isolation_branch2_hook "Филиал №2: изоляция — гость виден в филиале №2" GET "$BASE/api/v1/branches/branch2/guests"

run_step "Филиал №2: удаление гостя" DELETE "$BASE/api/v1/branches/branch2/guests/$branch2_guest_id" "" "branch2|guest|guest_id"

write_footer
generate_pdf
