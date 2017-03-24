# Krusty Kookies Sweden AB
EDA216 - Database Technology Project

## Authors
Andy Truong D13, dat13at1@student.lu.se

Aleksandar Simeunovic D13, dat13asi@student.lu.se

## Introduction
So the objective of this project has been to model and implement a database for
supervision of production and delivery of cookies produced by a company. The company
and its different routines where in need of a digitalisation because their scope
has grow since they have been bought by Krusty Kookies and could not to their procedures
manually anymore.

## Requirements
Here is a list of requirements we have fullfilled according to their requirements
specification the company has given us:

* A pallet is created when the label is read at the entrance to the deep-freeze storage

* The pallet number, product name, and date and time of production is registered in the database. The pallet number is unique.

* At any time you are able to check how many pallets of a product that have been produced during a specific time

* When a pallet is produced, the raw materials storage is updated

* You can check the amount in store of each ingredient, and to see when, and how much of, an ingredient was last delivered into storage

* pallets in the deep-freeze storage may be blocked, An order to block a pallet will always come before the pallet has been delivered.

* You are able to trace pallets and get all their information based on their number, contents or
 which pallets that have been produced during a certain time interval.

 * You could find out which products that are blocked, and also which pallets that contain a certain blocked product

 * You are able to check which pallets that have been delivered to a given customer, and the date and time of delivery.

 * Orders are registered in the database.

 * You can see all orders that are to be delivered during a specific time period

 * delivered pallets are updated with customer data and date of delivery.


## System outline

## E/R diagram
An E/R diagram of the system can be seen in the figure below.

![ER](ER.png)

## Relational model
Customers(<strong>customer_name</strong>, address)

RawMaterials(<strong>material_name</strong>, material_amount, unit)

RawDeliveries(<strong>delivery_date</strong>, <strong><em>material_name</em></strong>, delivery_amount)

Recipes(<strong>recipe_name</strong>)

Ingredients(<strong><em>material_name</em></strong>, <strong><em>recipe_name</em></strong>, quantity)

Orders(<strong>order_id</strong>, <em>customer_name</em>, deliver_by_date)

AmountOrdered(<strong><em>order_id</em></strong>, <strong><em>recipe_name</em></strong>, amount)

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
  material_amount    REAL CHECK (material_amount >= 0),
  unit               VARCHAR(10) NOT NULL,
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
  quantity         REAL CHECK (quantity > 0),
  PRIMARY KEY (material_name, recipe_name),
  FOREIGN KEY (material_name) REFERENCES RawMaterials(material_name),
  FOREIGN KEY (recipe_name) REFERENCES Recipes(recipe_name)
);

CREATE TABLE Orders (
  order_id          INTEGER PRIMARY KEY,
  customer_name     TEXT,
  delivery_by_date  DATE NOT NULL,
  FOREIGN KEY (customer_name) REFERENCES Customers(customer_name)
);

CREATE TABLE AmountOrdered (
  order_id          INTEGER,
  recipe_name       TEXT,
  amount            INTEGER CHECK (amount > 0),
  PRIMARY KEY (order_id, recipe_name),
  FOREIGN KEY (order_id) REFERENCES Orders(order_id),
  FOREIGN KEY (recipe_name) REFERENCES Recipes(recipe_name)
);

CREATE TABLE Pallets (
  pallet_id       INTEGER PRIMARY KEY,
  location        TEXT NOT NULL,
  production_date DATE NOT NULL,
  blocked         BOOLEAN,
  recipe_name     TEXT,
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

## User's manual
