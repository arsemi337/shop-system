# Shop system

Simple Spring Boot system for shopping services. 

To run the application, a docker container with PostgreSQL database needs to be started. 
The following command defines such a container: 

```
docker run --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

Having the database run, the Spring Boot application can be started with the use of IntelliJ or the following command:

```
./mvnw spring-boot:run
```

After running, the application is available at *localhost:8080* and exposes its services through REST API endpoints. 
They are divided into three groups of resources: Client, Product, Purchase. These endpoints are: 

### Client
* POST **/api/v1/client** - creates a client's account

#### Request body

```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "email@email.com"
}
```

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "email@email.com"
}
```

* GET **/api/v1/client** - fetches a client's account

#### Request body

```json
{
  "email": "email@email.com"
}
```

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "email@email.com"
}
```

* DELETE **/api/v1/client** - soft deletes a client's account

#### Request body

```json
{
  "email": "email@email.com"
}
```

* PUT **/api/v1/client** - updates a client's account

#### Request body

```json
{
  "firstname": "John",
  "lastname": "Doe",
  "email": "email@email.com"
}
```

#### Response body

```json
{
  "id": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "firstname": "John",
  "lastname": "Doe",
  "email": "email@email.com"
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

### Purchase
* POST **/api/v1/purchase** - makes a purchase of given products bought by a specific user

#### Request body

```json
{
  "clientId": "83cb721b-e18c-4890-8d3d-fa9a8ede8283",
  "purchaseProducts": [
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
  "clientFirstname": "Jonh",
  "clientLastname": "Doe",
  "clientEmail": "email@email.com",
  "purchasedProducts": [
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
