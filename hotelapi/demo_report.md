<h1>Демо-отчет по API</h1>
<p class="meta">Дата: 2026-02-28T02:57:34+07:00</p>
<style>
  @page { margin: 8mm; }
  body { font-family: "DejaVu Serif", "Times New Roman", serif; font-size: 10pt; color: #111; }
  h1 { margin-bottom: 4px; }
  .meta { margin-top: 0; color: #444; }
  table { width: 466mm; border-collapse: collapse; margin-top: 12px; table-layout: fixed; }
  th, td { border: 1px solid #444; padding: 4px; vertical-align: top; }
  th { background: #f0f0f0; }
  .cell { white-space: pre-wrap; word-wrap: break-word; overflow-wrap: anywhere; word-break: break-word; hyphens: auto; font-family: "DejaVu Sans Mono", "Courier New", monospace; font-size: 9pt; line-height: 1.2; }
  .block { background: #f4f4f4; border-radius: 3px; padding: 4px; }
</style>
<table>
  <colgroup>
    <col style="width: 44mm;">
    <col style="width: 76mm;">
    <col style="width: 76mm;">
    <col style="width: 90mm;">
    <col style="width: 90mm;">
    <col style="width: 90mm;">
  </colgroup>
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
<tr>
  <td><div class="cell">Изначальное состояние</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">central.department count: 2<br>central.staff_central count: 2<br>central.supplier count: 17<br>central.supplier_branch count: 18</div></td>
  <td><div class="cell block">branch1.guest count: 15<br>branch1.room count: 15<br>branch1.reservation count: 15<br>branch1.staff count: 15<br>branch1.reservation_staff count: 15</div></td>
  <td><div class="cell block">branch2.guest count: 15<br>branch2.room count: 15<br>branch2.reservation count: 15<br>branch2.staff count: 15<br>branch2.reservation_staff count: 15</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 01.<br>Центр: список филиалов</div></td>
  <td><div class="cell block">GET /api/v1/central/branches</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;branchId&quot;: 1,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;branch1&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;address&quot;: &quot;г. Новосибирск, ул. Ленина, 10&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;branchId&quot;: 2,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;branch2&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;address&quot;: &quot;г. Новосибирск, пр. Красный, 25&quot;<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 02.<br>Центр: создание отдела</div></td>
  <td><div class="cell block">POST /api/v1/central/departments<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14,<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254&quot;<br>}</div></td>
  <td><div class="cell block">Таблица: central.department<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 03.<br>Центр: обновление отдела</div></td>
  <td><div class="cell block">PUT /api/v1/central/departments/14<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254 (обновлено)&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 200<br>{<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14,<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254 (обновлено)&quot;<br>}</div></td>
  <td><div class="cell block">Таблица: central.department<br>Добавлено: нет<br>Удалено: нет<br>Изменено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;before&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;after&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254 (обновлено)&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 04.<br>Центр: поиск отделов</div></td>
  <td><div class="cell block">GET /api/v1/central/departments?name=Отдел</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;departmentId&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254 (обновлено)&quot;<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 05.<br>Центр: создание сотрудника</div></td>
  <td><div class="cell block">POST /api/v1/central/staff<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254&quot;,<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;staffId&quot;: 14,<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254&quot;,<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14<br>}</div></td>
  <td><div class="cell block">Таблица: central.staff_central<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 06.<br>Центр: обновление сотрудника</div></td>
  <td><div class="cell block">PUT /api/v1/central/staff/14<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254 (обновлено)&quot;,<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14<br>}</div></td>
  <td><div class="cell block">HTTP 200<br>{<br>&nbsp;&nbsp;&quot;staffId&quot;: 14,<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254 (обновлено)&quot;,<br>&nbsp;&nbsp;&quot;departmentId&quot;: 14<br>}</div></td>
  <td><div class="cell block">Таблица: central.staff_central<br>Добавлено: нет<br>Удалено: нет<br>Изменено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;before&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;after&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254 (обновлено)&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 07.<br>Центр: список сотрудников по отделу</div></td>
  <td><div class="cell block">GET /api/v1/central/staff?departmentId=14</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staffId&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254 (обновлено)&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;departmentId&quot;: 14<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 08.<br>Центр: создание поставщика</div></td>
  <td><div class="cell block">POST /api/v1/central/suppliers<br>{<br>&nbsp;&nbsp;&quot;companyName&quot;: &quot;Снабжение 222254&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;supplierId&quot;: 29,<br>&nbsp;&nbsp;&quot;companyName&quot;: &quot;Снабжение 222254&quot;<br>}</div></td>
  <td><div class="cell block">Таблица: central.supplier<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;company_name&quot;: &quot;Снабжение 222254&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 09.<br>Центр: обновление поставщика</div></td>
  <td><div class="cell block">PUT /api/v1/central/suppliers/29<br>{<br>&nbsp;&nbsp;&quot;companyName&quot;: &quot;Снабжение 222254 (обновлено)&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 200<br>{<br>&nbsp;&nbsp;&quot;supplierId&quot;: 29,<br>&nbsp;&nbsp;&quot;companyName&quot;: &quot;Снабжение 222254 (обновлено)&quot;<br>}</div></td>
  <td><div class="cell block">Таблица: central.supplier<br>Добавлено: нет<br>Удалено: нет<br>Изменено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;before&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;company_name&quot;: &quot;Снабжение 222254&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;after&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;company_name&quot;: &quot;Снабжение 222254 (обновлено)&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 10.<br>Центр: привязка поставщика к филиалу №1</div></td>
  <td><div class="cell block">POST /api/v1/central/suppliers/29/branches/1</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;supplierId&quot;: 29,<br>&nbsp;&nbsp;&quot;branchId&quot;: 1<br>}</div></td>
  <td><div class="cell block">Таблица: central.supplier_branch<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;branch_id&quot;: 1<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 11.<br>Центр: список филиалов поставщика</div></td>
  <td><div class="cell block">GET /api/v1/central/suppliers/29/branches</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplierId&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;branchId&quot;: 1<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 12.<br>Центр: отвязка поставщика от филиала №1</div></td>
  <td><div class="cell block">DELETE /api/v1/central/suppliers/29/branches/1</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">Таблица: central.supplier_branch<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;branch_id&quot;: 1<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 13.<br>Центр: удаление поставщика</div></td>
  <td><div class="cell block">DELETE /api/v1/central/suppliers/29</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">Таблица: central.supplier<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;supplier_id&quot;: 29,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;company_name&quot;: &quot;Снабжение 222254 (обновлено)&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 14.<br>Центр: удаление отсутствующего поставщика (негативный)</div></td>
  <td><div class="cell block">DELETE /api/v1/central/suppliers/999999</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:41.61703239Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Поставщик с id = 999999 не найден&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 15.<br>Центр: удаление сотрудника</div></td>
  <td><div class="cell block">DELETE /api/v1/central/staff/14</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">Таблица: central.staff_central<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Иван Петров 222254 (обновлено)&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 16.<br>Центр: удаление отдела</div></td>
  <td><div class="cell block">DELETE /api/v1/central/departments/14</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">Таблица: central.department<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;department_id&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Отдел размещения 222254 (обновлено)&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 17.<br>Центр: обновление отсутствующего отдела (негативный)</div></td>
  <td><div class="cell block">PUT /api/v1/central/departments/999999<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Отсутствующий отдел&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:42.888794945Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Департамент с id = 999999 не найден&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 18.<br>Центр: получение отсутствующего сотрудника (негативный)</div></td>
  <td><div class="cell block">GET /api/v1/central/staff/999999</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:43.231956644Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Сотрудник центрального узла с id = 999999 не найден&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 19.<br>Филиал №1: создание гостя</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/guests<br>{<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225401&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225401&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.guest<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225401&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 20.<br>Филиал №1: обновление гостя</div></td>
  <td><div class="cell block">PUT /api/v1/branches/branch1/guests/27<br>{<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225402&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 200<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225402&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.guest<br>Добавлено: нет<br>Удалено: нет<br>Изменено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;before&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225401&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;after&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225402&quot;<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 21.<br>Филиал №1: создание номера</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/rooms<br>{<br>&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&quot;price&quot;: 3200<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&quot;price&quot;: 3200<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.room<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;price&quot;: 3200<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 22.<br>Филиал №1: обновление номера</div></td>
  <td><div class="cell block">PUT /api/v1/branches/branch1/rooms/38<br>{<br>&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&quot;price&quot;: 3500<br>}</div></td>
  <td><div class="cell block">HTTP 200<br>{<br>&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&quot;price&quot;: 3500<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.room<br>Добавлено: нет<br>Удалено: нет<br>Изменено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;before&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;price&quot;: 3200<br>&nbsp;&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;after&quot;: {<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;price&quot;: 3500<br>&nbsp;&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 23.<br>Филиал №1: бронь с отсутствующим гостем (негативный)</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/reservations<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 999999,<br>&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&quot;checkIn&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&quot;checkOut&quot;: &quot;2026-03-12&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 409<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:45.404530706Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 409,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Конфликт данных&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Нарушена ссылочная целостность. Проверьте идентификаторы связанных сущностей.&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 24.<br>Филиал №1: создание бронирования</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/reservations<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&quot;checkIn&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&quot;checkOut&quot;: &quot;2026-03-12&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;reservationId&quot;: 50,<br>&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&quot;checkIn&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&quot;checkOut&quot;: &quot;2026-03-12&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.reservation<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservation_id&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;check_in&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;check_out&quot;: &quot;2026-03-12&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 25.<br>Филиал №1: поиск бронирований по датам</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch1/reservations?from=2026-03-01&amp;to=2026-03-31</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservationId&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;roomId&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;checkIn&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;checkOut&quot;: &quot;2026-03-12&quot;<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 26.<br>Филиал №1: создание сотрудника</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/staff<br>{<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Ольга Громова 222254&quot;,<br>&nbsp;&nbsp;&quot;role&quot;: &quot;receptionist&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;staffId&quot;: 27,<br>&nbsp;&nbsp;&quot;name&quot;: &quot;Ольга Громова 222254&quot;,<br>&nbsp;&nbsp;&quot;role&quot;: &quot;receptionist&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.staff<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Ольга Громова 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;role&quot;: &quot;receptionist&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 27.<br>Филиал №1: привязка бронирования к сотруднику</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/reservations/50/staff/27</div></td>
  <td><div class="cell block">HTTP 201</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.reservation_staff<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservation_id&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 27<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 28.<br>Филиал №1: список сотрудников бронирования</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch1/reservations/50/staff</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservationId&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staffId&quot;: 27<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 29.<br>Филиал №1: отвязка сотрудника от бронирования</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch1/reservations/50/staff/27</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.reservation_staff<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservation_id&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 27<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 30.<br>Филиал №1: удаление бронирования</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch1/reservations/50</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.reservation<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;reservation_id&quot;: 50,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;check_in&quot;: &quot;2026-03-10&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;check_out&quot;: &quot;2026-03-12&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 31.<br>Филиал №1: удаление номера</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch1/rooms/38</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.room<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;room_id&quot;: 38,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;number&quot;: &quot;204&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;price&quot;: 3500<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 32.<br>Филиал №1: получение отсутствующего номера (негативный)</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch1/rooms/38</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:48.547492901Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Номер с id = 38 не найден&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 33.<br>Филиал №1: удаление гостя</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch1/guests/27</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.guest<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Сергей Смирнов 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225402&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 34.<br>Филиал №1: получение отсутствующего гостя (негативный)</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch1/guests/999999</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:49.129738902Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Гость с id = 999999 не найден&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 35.<br>Филиал №1: удаление сотрудника</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch1/staff/27</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch1.staff<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;staff_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;name&quot;: &quot;Ольга Громова 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;role&quot;: &quot;receptionist&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 36.<br>Филиал: неверный код филиала (негативный)</div></td>
  <td><div class="cell block">GET /api/v1/branches/branchx/guests</div></td>
  <td><div class="cell block">HTTP 404<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:49.732257189Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 404,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Не найдено&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Филиал &#x27;branchx&#x27; не найден (ожидается: branch1 или branch2)&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 37.<br>Филиал №1: неверная цена номера (негативный)</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/rooms<br>{<br>&nbsp;&nbsp;&quot;number&quot;: &quot;666&quot;,<br>&nbsp;&nbsp;&quot;type&quot;: &quot;standard&quot;,<br>&nbsp;&nbsp;&quot;price&quot;: -1<br>}</div></td>
  <td><div class="cell block">HTTP 409<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:50.144595583Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 409,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Конфликт данных&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Цена номера не может быть отрицательной.&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 38.<br>Филиал №1: неверные даты бронирования (негативный)</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch1/reservations<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 1,<br>&nbsp;&nbsp;&quot;roomId&quot;: 1,<br>&nbsp;&nbsp;&quot;checkIn&quot;: &quot;2026-04-10&quot;,<br>&nbsp;&nbsp;&quot;checkOut&quot;: &quot;2026-04-05&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 409<br>{<br>&nbsp;&nbsp;&quot;timestamp&quot;: &quot;2026-02-27T19:57:50.592460903Z&quot;,<br>&nbsp;&nbsp;&quot;status&quot;: 409,<br>&nbsp;&nbsp;&quot;error&quot;: &quot;Конфликт данных&quot;,<br>&nbsp;&nbsp;&quot;message&quot;: &quot;Дата заезда должна быть меньше или равна дате выезда.&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 39.<br>Филиал №2: создание гостя</div></td>
  <td><div class="cell block">POST /api/v1/branches/branch2/guests<br>{<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Мария Орлова 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225403&quot;<br>}</div></td>
  <td><div class="cell block">HTTP 201<br>{<br>&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&quot;fullName&quot;: &quot;Мария Орлова 222254&quot;,<br>&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225403&quot;<br>}</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch2.guest<br>Добавлено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Мария Орлова 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225403&quot;<br>&nbsp;&nbsp;}<br>]<br>Удалено: нет<br>Изменено: нет</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 40.<br>Филиал №2: изоляция — гость не виден в филиале №1</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch1/guests</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 1,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Иванов Иван&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-01&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 2,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Петрова Мария&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-02&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 3,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сидоров Алексей&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-03&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 4,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Смирнова Екатерина&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-04&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 5,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Кузнецов Дмитрий&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-05&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 6,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Попова Анна&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-06&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 7,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Волков Сергей&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-07&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 8,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Васильева Ольга&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-08&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 9,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Морозов Павел&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-09&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 10,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Николаева Ирина&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-10&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 11,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Федоров Михаил&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-11&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 12,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Котова Наталья&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-12&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 13,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Орлов Артем&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-13&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Белова Юлия&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-14&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 15,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Захаров Кирилл&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-15&quot;<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Гость из филиала №2 в филиале №1: нет</div></td>
  <td><div class="cell block">—</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 41.<br>Филиал №2: изоляция — гость виден в филиале №2</div></td>
  <td><div class="cell block">GET /api/v1/branches/branch2/guests</div></td>
  <td><div class="cell block">HTTP 200<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 1,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Иванов Иван&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-01&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 2,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Петрова Мария&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-02&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 3,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Сидоров Алексей&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-03&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 4,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Смирнова Екатерина&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-04&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 5,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Кузнецов Дмитрий&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-05&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 6,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Попова Анна&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-06&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 7,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Волков Сергей&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-07&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 8,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Васильева Ольга&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-08&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 9,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Морозов Павел&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-09&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 10,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Николаева Ирина&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-10&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 11,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Федоров Михаил&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-11&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 12,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Котова Наталья&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-12&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 13,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Орлов Артем&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-13&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 14,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Белова Юлия&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-14&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 15,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Захаров Кирилл&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+7 913 555-00-15&quot;<br>&nbsp;&nbsp;},<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guestId&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;fullName&quot;: &quot;Мария Орлова 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225403&quot;<br>&nbsp;&nbsp;}<br>]</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Гость из филиала №2 в филиале №2: да</div></td>
</tr>
<tr>
  <td><div class="cell">Шаг 42.<br>Филиал №2: удаление гостя</div></td>
  <td><div class="cell block">DELETE /api/v1/branches/branch2/guests/27</div></td>
  <td><div class="cell block">HTTP 204</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">—</div></td>
  <td><div class="cell block">Таблица: branch2.guest<br>Добавлено: нет<br>Удалено:<br>[<br>&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;guest_id&quot;: 27,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;full_name&quot;: &quot;Мария Орлова 222254&quot;,<br>&nbsp;&nbsp;&nbsp;&nbsp;&quot;phone&quot;: &quot;+799922225403&quot;<br>&nbsp;&nbsp;}<br>]<br>Изменено: нет</div></td>
</tr>
  </tbody>
</table>
