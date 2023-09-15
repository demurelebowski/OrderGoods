# Spring Boot Order API

This is a Spring Boot application written in Java 11 that serves as a REST API for creating and managing orders for various products, such as iPhones.

## Getting Started

### Prerequisites

Before you begin, make sure you have the following tools installed on your machine:

- Docker
- Docker Compose

### Setting Up the Database

To set up the database, follow these steps:

1. Open a terminal and navigate to the project's root directory.
2. Enter the `docker` directory.
3. Run the following command to create a Docker container with a PostgreSQL database and populate it with sample data:

   docker-compose up -d

This command will create a PostgreSQL database and configure it with the necessary resources for testing the API endpoints.

### Accessing Swagger Documentation

The API documentation is available through Swagger. You can access it by opening your web browser and navigating to the following URL:

http://localhost:8080/swagger-ui.html

## Examples

Here are some example requests that you can make to the API:

### Get All Products

To retrieve a list of all products, send a GET request to the following endpoint:

```
GET http://localhost:8080/products
```

### Get a Specific Order

To retrieve information about a specific order, send a GET request to the following endpoint, replacing `{order_id}` with the actual order ID:

```
GET http://localhost:8080/orders/{order_id}
```

### Create a New Order

To create a new order, send a POST request to the following endpoint with a JSON request body similar to the example below:

```
POST http://localhost:8080/orders
```

Example JSON request body:

```json
{
    "client": {
        "id": 5
    },
    "items": [
        {
            "id": 2,
            "quantity": 1,
            "price": 99.9
        }
    ]
}
```

### Change Order Status to "PAID"

To change the status of an order to "PAID" send a PUT request to the following endpoint with a JSON request body similar to the example below:

```
PUT http://localhost:8080/orders
```

Example JSON request body:

```json
{
  "id": 2,
  "status": "PAID"
}
```
