# Krusty Kookies Sweden AB
EDA216 - Database Technology Project

## Authors
Andy Truong D13, dat13at1@student.lu.se

Aleksandar Simeunovic D13, dat13asi@student.lu.se

## Introduction

## Requirements

## System outline

## E/R diagram
An E/R diagram of the system can be seen in the figure below.

![ER](ER.png)

## Relational model
Customers(<strong>customer_name</strong>, address)

RawMaterials(<strong>material_name</strong>, material_amount)

RawDeliveries(<strong>delivery_date</strong>, <strong><em>material_name</em></strong>, delivery_amount)

Recipes(<strong>recipe_name</strong>)

Ingredients(<strong><em>material_name</em></strong>, <strong><em>recipe_name</em></strong>, quantity, unit)

Orders(<strong>order_id</strong>, <strong><em>recipe_name</em></strong>, amount, <em>customer_name</em>, deliver_by_date)

Pallets(<strong>pallet_id</strong>, location, production_date, blocked, <em>recipe_name</em>)

Shipments(<strong><em>order_id</em></strong>, <strong><em>pallet_id</em></strong>, date_of_delivery)

The relations are in BCNF since they have no functional dependencies except for the key dependencies.

## SQL statements
The SQL statements for creating the tables can be seen below.

```
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
```

## Users manual
