DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   title VARCHAR(255),
   author VARCHAR(255),
   genre VARCHAR(255),
   publishing_house VARCHAR(255),
   price DECIMAL,
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);