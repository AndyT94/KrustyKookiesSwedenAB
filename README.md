# KrustyKookiesSwedenAB
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

## Users manual
