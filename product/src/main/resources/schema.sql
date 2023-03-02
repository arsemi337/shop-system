DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   type VARCHAR(255),
   title VARCHAR(255),
   manufacturer VARCHAR(255),
   price DECIMAL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);