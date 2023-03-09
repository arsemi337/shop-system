DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS products;

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
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   customer_id UUID,
   date_time TIMESTAMP WITHOUT TIME ZONE,
   cost DECIMAL,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE orders ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customers (id);

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   title VARCHAR(255),
   type VARCHAR(255),
   manufacturer VARCHAR(255),
   price DECIMAL,
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE order_products (
  quantity INTEGER,
   product_id UUID NOT NULL,
   order_id UUID NOT NULL,
   CONSTRAINT pk_order_products PRIMARY KEY (product_id, order_id)
);

ALTER TABLE order_products ADD CONSTRAINT FK_ORDER_PRODUCTS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_products ADD CONSTRAINT FK_ORDER_PRODUCTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);