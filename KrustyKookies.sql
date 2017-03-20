-- Delete the tables if they exist.
-- Disable foreign key checks, so the tables can
-- be dropped in arbitrary order.
PRAGMA foreign_keys=OFF;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS RawMaterials;
DROP TABLE IF EXISTS RawDeliveries;
DROP TABLE IF EXISTS Recipes;
DROP TABLE IF EXISTS Ingredients;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Pallets;
DROP TABLE IF EXISTS Shipments;

PRAGMA foreign_keys=ON;

-- Create the tables.
CREATE TABLE Customers (
  customer_name      TEXT,
  address            TEXT,
  primary key (customer_name)
);

CREATE TABLE RawMaterials (
  material_name      TEXT,
  material_amount    INTEGER,
  primary key (material_name)
);

CREATE TABLE RawDeliveries (
  delivery_date         date,
  material_name         TEXT,
  delivery_amount       INTEGER,
  primary key(delivery_date,material_name)
  foreign key (material_name) references RawMaterials(material_name)
);

CREATE TABLE Recipes (
  recipe_name    TEXT,
  primary key (recipe_name)
);

CREATE TABLE Ingredients (
  material_name    TEXT,
  recipe_name      TEXT,
  quantity         INTEGER,
  primary key (material_name,recipe_name),
  foreign key (material_name) references RawMaterials(material_name),
  foreign key (recipe_name) references Recipes(recipe_name)
);
CREATE TABLE Orders (
  order_id          INTEGER,
  recipe_name       TEXT,
  amount            INTEGER,
  customer_name     TEXT,
  delivery_by_date  date,
  primary key (order_id,recipe_name),
  foreign key (recipe_name) references Recipes(recipe_name),
  foreign key (customer_name) references Customers(customer_name)
);

CREATE TABLE Pallets (
  pallet_id       INTEGER,
  location        TEXT,
  production_date date,
  blocked         boolean,
  recipe_name     TEXT,
  primary key (pallet_id),
  foreign key (recipe_name) references Recipes(recipe_name)
);
CREATE TABLE Shipments (
  order_id          INTEGER,
  pallet_id         INTEGER,
  date_of_delivery  date,
  primary key (order_id),
  foreign key (pallet_id) references Pallets(pallet_id)
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
