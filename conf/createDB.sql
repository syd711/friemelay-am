DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  last_login DATETIME DEFAULT CURRENT_TIMESTAMP,
  customer_status VARCHAR(20),
  address_id INT(8),
  billing_address_id INT(8),
  email VARCHAR(80)  NOT NULL,
  customer_password VARCHAR(40),
  phone VARCHAR(50),
  newsletter INT(1) DEFAULT 0
);

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  company VARCHAR(100),
  additional VARCHAR(100),
  street VARCHAR(100),
  zip  VARCHAR(10) NOT NULL,
  city VARCHAR(100) NOT NULL,
  country VARCHAR(100) 
);


DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  last_change_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  order_status INT(8) NOT NULL DEFAULT 0,
  customer_id INT(8) NOT NULL DEFAULT 0,
  payment_type INT(1) NOT NULL,
  total_price DECIMAL(5,2) NOT NULL,
  shipping_costs DECIMAL(5,2) NOT NULL,
  customer_comments VARCHAR(2000),
  comments VARCHAR(2000)
);
ALTER TABLE orders AUTO_INCREMENT = 1356;

DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
  order_id INT(8) NOT NULL,
  amount INT(4) NOT NULL,
  price DECIMAL(5,2) NOT NULL,
  product_id VARCHAR(100),
  product_description VARCHAR(100) NOT NULL,
  url VARCHAR(200),
  image_url VARCHAR(200)
);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  model_type INT(4) NOT NULL,
  catalog_status INT(4) NOT NULL DEFAULT 0,
  parent_id INT(8) NULL,
  top_level INT(1) NOT NULL DEFAULT 0,
  title VARCHAR(100) NULL,
  short_description VARCHAR(300) NULL,
  details VARCHAR(2000) NULL,
  image MEDIUMBLOB
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  model_type INT(4) NOT NULL,
  catalog_status INT(4) NOT NULL DEFAULT 0,
  parent_id INT(8) NOT NULL,
  stock INT(8) NOT NULL DEFAULT 0,
  amount INT(1) NOT NULL DEFAULT 0,
  price DECIMAL(5,2) NOT NULL DEFAULT 0,
  title VARCHAR(100) NULL,
  short_description VARCHAR(300) NULL,
  variant_label VARCHAR(100) NULL,
  variant_name VARCHAR(100) NULL,
  variant_short_description VARCHAR(300) NULL,  
  details VARCHAR(2000) NULL,
  image MEDIUMBLOB
);

DROP TABLE IF EXISTS productimages;
CREATE TABLE productimages (
  id INT(8) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  product_id INT(8) NULL,
  mime_type VARCHAR(20) NULL,
  image MEDIUMBLOB,
  teaser_image MEDIUMBLOB,
  thumbnail_image MEDIUMBLOB  
);

