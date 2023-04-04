DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   name VARCHAR(255),
   type VARCHAR(255),
   manufacturer VARCHAR(255),
   price DECIMAL,
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);