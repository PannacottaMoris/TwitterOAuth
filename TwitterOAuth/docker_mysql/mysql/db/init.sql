CREATE DATABASE store;
use store;

CREATE TABLE goods (
  id int(10) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  price int(10) NOT NULL,
  description varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
);
