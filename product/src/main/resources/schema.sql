DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id UUID NOT NULL,
   creation_time TIMESTAMP WITHOUT TIME ZONE,
   type VARCHAR(255),
   title VARCHAR(255),
   publishingHouse VARCHAR(255),
   price DECIMAL,
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_products PRIMARY KEY (id)
);

ALTER TABLE products ADD CONSTRAINT uc_products_title UNIQUE (title);