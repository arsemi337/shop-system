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

Having the database run, the Spring Boot application in *customer* directory can be started with the use of IntelliJ or the following command:

```
./mvnw spring-boot:run
```

After running, the application is available at *localhost:8080* and exposes its services through REST API endpoints. 
The REST API can be also accessed through the Swagger UI tool, available at *localhost:8080/*
They are divided into three groups of resources: Customer, Product, Purchase. These endpoints are: 

### Customer
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
