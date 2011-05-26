--Database table creation

--Table to store user information for connecting to web service
--Only the basic need to be stored since the account should exist on the website already
--Todo: have a method for creating an account if you don't have one
--Todo: how to store a local password for use with a web service and have it be secure from snooping?
CREATE TABLE users (
_id integer primary key autoincrement,
localid integer not null,
username text UNIQUE,
email text,
state text,
zipcode text,
passwordhash text
)

--Table for storing records of observations
CREATE TABLE records (
_id integer primary key autoincrement,
username text,
species text,
--should we link to allow multiple photos per spot?
photo,
lat REAL,
lon REAL,
time text,
uploaded integer default 0,
FOREIGN KEY (username) REFERENCES users(username)
)


CREATE TABLE species (
_id integer primary key autoincrement,
"id" integer,
"category" text,
"subcat" text,
"common" text,
"scientific" text,
"cwhr" text,
"ITIS" integer
)

--INSERT INTO species SELECT id,category,subcat,common,scientific,cwhr,ITIS FROM species1

--Create a list of all the possible choices
CREATE VIEW allspp AS
SELECT DISTINCT category as list FROM species
UNION
SELECT DISTINCT subcat FROM species WHERE NOT NULL
UNION
SELECT common FROM species
UNION
SELECT scientific FROM species

