--Database table creation

--Table to store user information for connecting to web service
--Only the basic need to be stored since the account should exist on the website already
--Todo: have a method for creating an account if you don't have one
--Todo: how to store a local password for use with a web service and have it be secure from snooping?
CREATE TABLE users (
localid int not null,
username text,
email text,
state text,
zipcode text,
passwordhash text,
)

--Table for storing records of observations
CREATE TABLE records (
username ForeignKey,
)
