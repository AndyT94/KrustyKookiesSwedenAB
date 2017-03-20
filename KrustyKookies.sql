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
  address            TEXT NOT NULL,
  PRIMARY KEY (customer_name)
);

CREATE TABLE RawMaterials (
  material_name      TEXT,
  material_amount    INTEGER CHECK (material_amount >= 0),
  PRIMARY KEY (material_name)
);

CREATE TABLE RawDeliveries (
  delivery_date         DATE,
  material_name         TEXT,
  delivery_amount       INTEGER CHECK (delivery_amount >= 0),
  PRIMARY KEY (delivery_date, material_name),
  FOREIGN KEY (material_name) REFERENCES RawMaterials(material_name)
);

CREATE TABLE Recipes (
  recipe_name    TEXT,
  PRIMARY KEY (recipe_name)
);

CREATE TABLE Ingredients (
  material_name    TEXT,
  recipe_name      TEXT,
  quantity         INTEGER CHECK (quantity > 0),
  unit             VARCHAR(10) NOT NULL,
  PRIMARY KEY (material_name, recipe_name),
  FOREIGN KEY (material_name) REFERENCES RawMaterials(material_name),
  FOREIGN KEY (recipe_name) REFERENCES Recipes(recipe_name)
);

CREATE TABLE Orders (
  order_id          INTEGER AUTO_INCREMENT,
  recipe_name       TEXT,
  amount            INTEGER CHECK (amount > 0),
  customer_name     TEXT,
  delivery_by_date  DATE NOT NULL,
  PRIMARY KEY (order_id, recipe_name),
  FOREIGN KEY (recipe_name) REFERENCES Recipes(recipe_name),
  FOREIGN KEY (customer_name) REFERENCES Customers(customer_name)
);

CREATE TABLE Pallets (
  pallet_id       INTEGER AUTO_INCREMENT,
  location        TEXT NOT NULL,
  production_date DATE NOT NULL,
  blocked         BOOLEAN,
  recipe_name     TEXT,
  PRIMARY KEY (pallet_id),
  FOREIGN KEY (recipe_name) REFERENCES Recipes(recipe_name)
);

CREATE TABLE Shipments (
  order_id          INTEGER,
  pallet_id         INTEGER,
  date_of_delivery  DATE,
  PRIMARY KEY (order_id, pallet_id),
  FOREIGN KEY (pallet_id) REFERENCES Pallets(pallet_id),
  FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

-- Insert data into the tables.
INSERT INTO Customers (customer_name, address) VALUES
('Finkakor AB', 'Helsingborg'),
('Småbröd AB', 'Malmö'),
('Kaffebröd AB', 'Landskrona'),
('Bjudkakor AB', 'Ystad'),
('Klaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Gästkakor AB', 'Hässleholm'),
('Skånekakor AB', 'Perstorp');

INSERT INTO Ingredients (material_name, recipe_name, quantity, unit) VALUES
('Nut ring', 'Flour', 450, 'g'),
('Nut ring', 'Butter', 450, 'g'),
('Nut ring', 'Icing sugar', 190, 'g'),
('Nut ring', 'Roasted, chopped nuts', 225, 'g'),
('Nut cookie', 'Fine-ground nuts', 750, 'g'),
('Nut cookie', 'Ground, roasted nuts', 625, 'g'),
('Nut cookie', 'Bread crumbs', 125, 'g'),
('Nut cookie', 'Sugar', 375, 'g'),
('Nut cookie', 'Egg whites', 3.5, 'dl'),
('Nut cookie', 'Chocolate', 50, 'g'),
('Amneris', 'Marzipan', 750, 'g'),
('Amneris', 'Butter', 250, 'g'),
('Amneris', 'Eggs', 250, 'g'),
('Amneris', 'Potato starch', 25, 'g'),
('Amneris', 'Wheat flour', 25, 'g'),
('Tango', 'Butter', 200, 'g'),
('Tango', 'Sugar', 250, 'g'),
('Tango', 'Flour', 300, 'g'),
('Tango', 'Sodium bicarbonate', 4, 'g'),
('Tango', 'Vanilla', 2, 'g'),
('Almond delight', 'Butter', 400, 'g'),
('Almond delight', 'Sugar', 270, 'g'),
('Almond delight', 'Chopped almonds', 279, 'g'),
('Almond delight', 'Flour', 400, 'g'),
('Almond delight', 'Cinnamon', 10, 'g'),
('Berliner', 'Flour', 350, 'g'),
('Berliner', 'Butter', 250, 'g'),
('Berliner', 'Icing sugar', 100, 'g'),
('Berliner', 'Eggs', 50, 'g'),
('Berliner', 'Vanilla sugar', 5, 'g'),
('Berliner', 'Chocolate', 50, 'g');
-- And re-enable foreign key checks.

PRAGMA foreign_key = on;
