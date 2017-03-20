-- Delete the tables if they exist.
-- Disable foreign key checks, so the tables can
-- be dropped in arbitrary order.
PRAGMA foreign_keys=OFF;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS performances;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS theaters;

PRAGMA foreign_keys=ON;

-- Create the tables.
CREATE TABLE users (
  username      VARCHAR(15),
  name          TEXT NOT NULL,
  address       TEXT,
  phone_number  CHAR(10) NOT NULL,
  primary key (username)
);

CREATE TABLE performances (
  name    TEXT,
  date    DATE,
  theater_name TEXT,
  available_seats INTEGER check (available_seats >= 0),
  primary key (name, date),
  foreign key (name) references movies(name),
  foreign key (theater_name) references theaters(name)
);

CREATE TABLE reservations (
  reservation_number    INTEGER PRIMARY KEY,
  username              VARCHAR(15),
  movie_name            TEXT,
  date                  DATE,
  foreign key (movie_name, date) references performances(name, date),
  foreign key (username) references users(username)
);

CREATE TABLE movies (
  name    TEXT,
  primary key (name)
);

CREATE TABLE theaters (
  name    TEXT,
  seats   INTEGER NOT NULL CHECK (seats > 0),
  primary key (name)
);

-- Insert data into the tables.
INSERT INTO users (username, name, address, phone_number) VALUES
('andy', 'AndyTruong', 'LTH', 0789456321),
('andy2', 'Andy2', NULL, 0123456789),
('andy3123', 'Andy3', 'dawdaw', 0258695847);

INSERT INTO theaters (name, seats) VALUES
('Royal', 498),
('Filmstaden Entré', 60),
('Filmstaden Storgatan', 63);

INSERT INTO movies (name) VALUES
('Alfons leker Einstein'),
('Allied'),
('American Pastoral'),
('Arrival'),
('Assassin''s Creed'),
('Bamse och häxans dotter'),
('Cosi fan tutte - opera från Parisoperan'),
('Elle'),
('En alldeles särskild dag - Klassiker'),
('En man som heter Ove'),
('En midommarnattsdröm - balett från Parisoperan'),
('Fifty shades darker'),
('Filmen om Badrock'),
('Fyren mellan haven'),
('Hundraettåringen som smet från notan och försvann'),
('Jackie'),
('Jag, Daniel Blake'),
('Jätten'),
('La La Land'),
('Lion'),
('Live by night'),
('Manchester by the sea'),
('Marcus & Martinus - Tillsammans mot drömmen'),
('Min pappa Tonni Erdmann'),
('Måste Gitt'),
('Passengers'),
('Pettson & Findus - Juligheter'),
('Resident Evil - The Final Chapter'),
('Rings'),
('Rogue One: A Star Wars Story'),
('Sing'),
('Skönheten i allt'),
('Split'),
('The Bye Bye Man'),
('The Handmaiden'),
('The Lego Batman Movie'),
('Trolls'),
('Trubaduren - opera från Royal Opera House'),
('Törnrosa - balett från Royal Opera House'),
('Vaiana'),
('Vem lurar Alfons'),
('xXx: The Return of Xander Cage');

INSERT INTO performances (name, date, theater_name, available_seats) VALUES
('La La Land', '2017-02-01', 'Royal', 496),
('La La Land', '2017-02-02', 'Filmstaden Storgatan', 63),
('Jätten', '2017-02-02', 'Filmstaden Storgatan', 63);

INSERT INTO reservations (username, movie_name, date) VALUES
('andy', 'La La Land', '2017-02-01'),
('andy2', 'La La Land', '2017-02-01');

-- And re-enable foreign key checks.

PRAGMA foreign_key = on;
