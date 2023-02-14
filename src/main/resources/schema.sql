CREATE TABLE client (
   id UUID NOT NULL,
   firstname VARCHAR(255),
   lastname VARCHAR(255),
   email VARCHAR(255),
   is_deleted BOOLEAN NOT NULL,
   CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE product (
   id UUID NOT NULL,
   type VARCHAR(255),
   title VARCHAR(255),
   manufacturer VARCHAR(255),
   price DECIMAL,
   CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE purchase (
   id UUID NOT NULL,
   client_id UUID,
   date_time TIMESTAMP WITHOUT TIME ZONE,
   cost DECIMAL,
   CONSTRAINT pk_purchase PRIMARY KEY (id)
);

CREATE TABLE purchase_product (
   quantity INTEGER,
   product_id UUID NOT NULL,
   purchase_id UUID NOT NULL,
   CONSTRAINT pk_purchaseproduct PRIMARY KEY (product_id, purchase_id)
);

ALTER TABLE purchase_product ADD CONSTRAINT FK_PURCHASEPRODUCT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE purchase_product ADD CONSTRAINT FK_PURCHASEPRODUCT_ON_PURCHASE FOREIGN KEY (purchase_id) REFERENCES purchase (id);