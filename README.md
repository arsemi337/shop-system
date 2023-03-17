# Shop system

Simple Spring Boot system for shopping services. 

To run the application, two docker containers with PostgreSQL database need to be started. 
The following commands defines those containers: 

```
docker run --name customerPostgreSQL -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

```
docker run --name productPostgreSQL -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5431:5432 -d postgres
```

Additionally, the application requires a running Kafka service, which can be started by executing the following command in the main project directory: 

```
docker-compose up -d
```

The application is composed of four projects. They are present in the main repository directory, the structure of which is:  

    .
    ├── customer                          # *Customer* project for creating users and making their orders
    ├── product                           # *Product* project for adding, fetching, modifying, and removing products
    ├── common-lib                        # *common-lib* library containing classes and services commonly used in other projects
    ├── validation-demo                   # *Validation Demo* application used for testing poison pills handling in Kafka consumer service
    ├── docker-compose.yaml               # Docker compose file defining Kafka service containers
    ├── shop_system.postman_collection    # Postman collection containing exemplary requests, which can be used for testing purposes
    └── README.md

Having the database and Kafka run, *Customer*, *Product*, and *ValidationDemo* applications can be started with the use of IntelliJ or the following command: 

```
./mvnw spring-boot:run
```

After running, the applications are available at the following URLs: 
* Customer - *localhost:8080*
* Product - *localhost:8085*
* Validation Demo* - localhost:8090*

Each application exposes its services through REST API endpoints presented below. 
The APIs can be accessed through the *Swagger UI* tool, available at the root path (*/*). 

After running, the application is available at ** and exposes its services through REST API endpoints. 
The REST API can be also accessed through the Swagger UI tool, available at *localhost:8080/*

Running the applications results in adding initial data to the database, which can be used to test the API. 
Initial data contains the following lists: 

#### Customers

|   | id                                     | creation_time         | firstname  | lastname   | email             | is_deleted |
|---|----------------------------------------|-----------------------|------------|------------|-------------------|------------|
| 1 | "ebb0e67e-ac5d-11ed-afa1-0242ac120002" | "2004-10-19 10:23:54" | "Prescott" | "Rutledge" | "prescott@sii.pl" | false      |
| 2 | "ebb0e9f8-ac5d-11ed-afa1-0242ac120002" | "2004-10-19 10:24:54" | "Jonas"    | "Buckner"  | "jonas@sii.pl"    | false      |
| 3 | "ebb0ebd8-ac5d-11ed-afa1-0242ac120002" | "2004-10-19 10:25:54" | "Regan"    | "Baird"    | "regan@sii.pl"    | false      |
| 4 | "ebb0edb7-ac5d-11ed-afa1-0242ac120002" | "2004-10-19 10:26:54" | "Ursula"   | "Butler"   | "ursula@sii.pl"   | false      |
| 5 | "ebb0ef5c-ac5d-11ed-afa1-0242ac120002" | "2004-10-19 10:27:54" | "Natalie"  | "Richards" | "natalie@sii.pl"  | false      |

#### Products

|   | id                                     | creation_time         | title              | type           | manufacturer | price   | is_deleted |
|---|----------------------------------------|-----------------------|--------------------|----------------|--------------|---------|------------|
| 1 | "7a9c839a-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:23:54" | "Pixel 6"          | "Smartphone"   | "Google"     | 479.99  | false      |
| 2 | "7a9c87a0-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:24:54" | "Swift 3"          | "Laptop"       | "Acer"       | 809.99  | false      |
| 3 | "7a9c89e4-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:25:54" | "Mavic Mini 2"     | "Drone"        | "DJI"        | 565.99  | false      |
| 4 | "7a9c8c64-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:26:54" | "GeForce RTX 4090" | "Graphic card" | "MSI"        | 2729.99 | false      |
| 5 | "7a9c9100-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:27:54" | "i7-10700F"        | "Processor"    | "Intel"      | 249.99  | false      |
| 6 | "abdb0f08-ac5c-11ed-afa1-0242ac120002" | "2004-10-19 10:28:54" | "P2422H"           | "Monitor"      | "Dell"       | 199     | false      |

### Customer
Endpoints of this service are divided into three groups of resources: Customer, Product, Purchase. These endpoints are: 
* POST **/api/v1/customer** - creates a customer's account

#### Request body

```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "newEmail@newEmail.com"
}
```

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "newEmail@newEmail.com"
}
```

* GET **/api/v1/customer** - fetches a customer's account

Customer ID is passed to the service as a path variable. The ID needs to have a UUID form. 

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "newEmail@newEmail.com"
}
```

* DELETE **/api/v1/customer** - soft deletes a customer's account

#### Request body

```json
{
  "email": "newEmail@newEmail.com"
}
```

* PUT **/api/v1/customer** - updates a customer's account

#### Request body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "newFirstname": "John",
  "newLastname": "Doe",
  "newEmail": "newEmail@newEmail.com"
}
```

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "newEmail@newEmail.com"
}
```

### Product
* GET **/api/v1/product/{id}** - fetches an available product by its id

Product ID is passed to the service as a path variable. The ID needs to have a UUID form. 

#### Response body

```json
{
  "id": "7a9c839a-ac5c-11ed-afa1-0242ac120002",
  "type": "Smartphone",
  "title": "Pixel 6",
  "manufacturer": "Google",
  "price": 479.99
}
```

* POST **/api/v1/product** - fetches a paginated list of all available products

Service takes pagination request parameters. The two most common ones are *page* and *size*, 
which take page number and page size, correspondingly. 

#### Response body

```json
{
  "content": [
    {
      "id": "7a9c839a-ac5c-11ed-afa1-0242ac120002",
      "type": "Smartphone",
      "title": "Pixel 6",
      "manufacturer": "Google",
      "price": 479.99
    },
    ...
  ],
  ...
}
```

### Order
* POST **/api/v1/order** - makes a order of given products bought by a specific user

#### Request body

```json
{
  "customerId": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "orderProducts": [
    {
      "productId": "7a9c839a-ac5c-11ed-afa1-0242ac120002",
      "quantity": 10
    },
    ...
  ]
}
```

#### Response body

```json
{
  "customerFirstname": "Jonh",
  "customerLastname": "Doe",
  "customerEmail": "newEmail@newEmail.com",
  "orderedProducts": [
    {
      "title": "Pixel 6",
      "type": "Smartphone",
      "manufacturer": "Google",
      "price": 479.99,
      "quantity": 10
    },
    ...
  ],
  "totalCost": 5609.89
}
```

### Validation Demo
* POST **/model** - sends an exemplary model to Kafka *product* topic of the application.
  This model is invalid in terms of the application functioning. Thus, this service can be used to test
  the behaviour of poison pill handling of the *Customer* application. 

#### Response body

```json
"Sort of name" model with number 10 has been sent to the topic with a PRODUCT_CREATED header
```