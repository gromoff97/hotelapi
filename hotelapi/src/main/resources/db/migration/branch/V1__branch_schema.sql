-- Branch schema, indexes, procedures, triggers, seed (branch_db)

CREATE TABLE guest (
  guest_id SERIAL PRIMARY KEY,
  full_name TEXT,
  phone TEXT
);

CREATE TABLE room (
  room_id SERIAL PRIMARY KEY,
  number TEXT,
  type TEXT,
  price NUMERIC
);

CREATE TABLE staff (
  staff_id SERIAL PRIMARY KEY,
  name TEXT,
  role TEXT
);

CREATE TABLE reservation (
  reservation_id SERIAL PRIMARY KEY,
  guest_id INT NOT NULL,
  room_id INT NOT NULL,
  check_in DATE,
  check_out DATE
);

CREATE TABLE reservation_staff (
  reservation_id INT NOT NULL,
  staff_id INT NOT NULL,
  PRIMARY KEY (reservation_id, staff_id)
);

ALTER TABLE reservation
  ADD CONSTRAINT fk_reservation_guest
  FOREIGN KEY (guest_id) REFERENCES guest(guest_id);

ALTER TABLE reservation
  ADD CONSTRAINT fk_reservation_room
  FOREIGN KEY (room_id) REFERENCES room(room_id);

ALTER TABLE reservation_staff
  ADD CONSTRAINT fk_reservation_staff_reservation
  FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id);

ALTER TABLE reservation_staff
  ADD CONSTRAINT fk_reservation_staff_staff
  FOREIGN KEY (staff_id) REFERENCES staff(staff_id);

CREATE INDEX IF NOT EXISTS idx_reservation_guest_id ON reservation(guest_id);
CREATE INDEX IF NOT EXISTS idx_reservation_room_id ON reservation(room_id);
CREATE INDEX IF NOT EXISTS idx_reservation_check_in ON reservation(check_in);
CREATE INDEX IF NOT EXISTS idx_reservation_staff_staff_id ON reservation_staff(staff_id);
CREATE INDEX IF NOT EXISTS idx_reservation_staff_reservation_id ON reservation_staff(reservation_id);
CREATE INDEX IF NOT EXISTS idx_staff_role ON staff(role);

CREATE OR REPLACE FUNCTION trg_check_reservation_dates()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
  IF NEW.check_in > NEW.check_out THEN
    RAISE EXCEPTION 'check_in must be <= check_out';
  END IF;
  RETURN NEW;
END;
$$;

CREATE TRIGGER check_reservation_dates
BEFORE INSERT OR UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION trg_check_reservation_dates();

CREATE OR REPLACE FUNCTION trg_room_price_non_negative()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
  IF NEW.price IS NOT NULL AND NEW.price < 0 THEN
    RAISE EXCEPTION 'price must be non-negative';
  END IF;
  RETURN NEW;
END;
$$;

CREATE TRIGGER check_room_price
BEFORE INSERT OR UPDATE ON room
FOR EACH ROW
EXECUTE FUNCTION trg_room_price_non_negative();

CREATE OR REPLACE PROCEDURE sp_add_guest(p_full_name TEXT, p_phone TEXT)
LANGUAGE plpgsql
AS $$
BEGIN
  INSERT INTO guest (full_name, phone) VALUES (p_full_name, p_phone);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_guest(p_guest_id INT, p_full_name TEXT, p_phone TEXT)
LANGUAGE plpgsql
AS $$
BEGIN
  UPDATE guest
  SET full_name = p_full_name,
      phone = p_phone
  WHERE guest_id = p_guest_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_delete_guest(p_guest_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
  DELETE FROM guest WHERE guest_id = p_guest_id;
END;
$$;

CREATE OR REPLACE PROCEDURE sp_add_reservation(
  p_guest_id INT,
  p_room_id INT,
  p_check_in DATE,
  p_check_out DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
  INSERT INTO reservation (guest_id, room_id, check_in, check_out)
  VALUES (p_guest_id, p_room_id, p_check_in, p_check_out);
END;
$$;

INSERT INTO guest (full_name, phone) VALUES
  ('Иванов Иван', '+7 913 555-00-01'),
  ('Петрова Мария', '+7 913 555-00-02'),
  ('Сидоров Алексей', '+7 913 555-00-03'),
  ('Смирнова Екатерина', '+7 913 555-00-04'),
  ('Кузнецов Дмитрий', '+7 913 555-00-05'),
  ('Попова Анна', '+7 913 555-00-06'),
  ('Волков Сергей', '+7 913 555-00-07'),
  ('Васильева Ольга', '+7 913 555-00-08'),
  ('Морозов Павел', '+7 913 555-00-09'),
  ('Николаева Ирина', '+7 913 555-00-10'),
  ('Федоров Михаил', '+7 913 555-00-11'),
  ('Котова Наталья', '+7 913 555-00-12'),
  ('Орлов Артем', '+7 913 555-00-13'),
  ('Белова Юлия', '+7 913 555-00-14'),
  ('Захаров Кирилл', '+7 913 555-00-15');

INSERT INTO room (number, type, price)
SELECT
  (100 + gs)::text,
  CASE
    WHEN gs % 3 = 0 THEN 'suite'
    WHEN gs % 2 = 0 THEN 'double'
    ELSE 'single'
  END,
  (3000 + gs * 100)::numeric
FROM generate_series(1, 15) AS gs;

INSERT INTO staff (name, role) VALUES
  ('Громов Андрей', 'receptionist'),
  ('Ильина Светлана', 'chef'),
  ('Савельев Игорь', 'cleaner'),
  ('Кузьмина Дарья', 'receptionist'),
  ('Мельников Олег', 'chef'),
  ('Беляева Татьяна', 'cleaner'),
  ('Егоров Денис', 'receptionist'),
  ('Гусева Марина', 'chef'),
  ('Романов Артур', 'cleaner'),
  ('Тарасова Елена', 'receptionist'),
  ('Фролов Максим', 'chef'),
  ('Доронина Полина', 'cleaner'),
  ('Комаров Никита', 'receptionist'),
  ('Лебедева Ксения', 'chef'),
  ('Семенов Павел', 'cleaner');

INSERT INTO reservation (guest_id, room_id, check_in, check_out)
SELECT
  gs,
  gs,
  DATE '2026-01-01' + (gs - 1),
  DATE '2026-01-02' + (gs - 1)
FROM generate_series(1, 15) AS gs;

INSERT INTO reservation_staff (reservation_id, staff_id)
SELECT gs, gs
FROM generate_series(1, 15) AS gs;
