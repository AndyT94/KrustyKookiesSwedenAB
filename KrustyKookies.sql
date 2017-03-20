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
INSERT INTO Customers (customer_name,address) VALUES
('Finkakor AB','Helsingborg'),
('Småbröd AB','Malmö'),
('Kaffebröd AB','Landskorna'),
('Bjudkakor AB','Ystad'),
('Klaskakor AB','Trelleborg'),
('Partykakor AB','Kristianstad'),
('Gästkakor AB','Hässleholm'),
('Skånekakor AB','Peterstorp'),


INSERT INTO Ingredients (material_name,recipe_name,delivery_amount) VALUES
('Nut ring','Flour',450),
('Nut ring','Butter',450),
('Nut ring','Icing sugar',190),
('Nut ring','Roasted,chopped nuts',225),
('Nut cookie','Fined-ground nuts',750),
('Nut cookie','Ground, roasted nuts',625),
('Nut cookie','Bread crumbs',125),
('Nut cookie','Sugar',375),
('Nut cookie','Egg whites',3.5),
('Nut cookie','Chocolate',50),
('Amneris','Marzipan',750),
('Amneris','Butter',250),
('Amneris','Eggs',250),
('Amneris','Potato starch',25),
('Amneris','Wheat flour',25),
('Tango','Butter',200),
('Tango','Sugar',250),
('Tango','Flour',300),
('Tango','Sodium bicarbonate',4),
('Tango','Vanilla',2),
('Almond delight','Butter',400),
('Almond delight','Sugar',270),
('Almond delight','Chopped almonds',279),
('Almond delight','Flour',400),
('Almond delight','Cinnamon',10),
('Berliner','Flour',350),
('Berliner','Butter',250),
('Berliner','Icing sugar',100),
('Berliner','Eggs',50),
('Berliner','Vanilla sugar',5),
('Berliner','Chocolate',50),








INSERT INTO movies (name) VALUES




-- And re-enable foreign key checks.

PRAGMA foreign_key = on;
