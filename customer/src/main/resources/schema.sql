DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   firstname VARCHAR(255),
   lastname VARCHAR(255),
   email VARCHAR(255),
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_customers PRIMARY KEY (id)
);

CREATE TABLE orders (
  id UUID NOT NULL,
   customer_id UUID,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   total_cost DECIMAL,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE orders ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customers (id);

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   name VARCHAR(255),
   type VARCHAR(255),
   manufacturer VARCHAR(255),
   price DECIMAL,
   is_deleted BOOLEAN NOT NULL,
   order_id UUID,
   CONSTRAINT pk_products PRIMARY KEY (id)
);

ALTER TABLE products ADD CONSTRAINT FK_PRODUCTS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);