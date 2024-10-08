# Order Management API

This project is a Spring Boot-based order management API platform that allows clients to sign up, log in, place orders, cancel orders, and fetch order history. 

## Features
- **Client Signup & Login**: Clients can register and log in to access the system.
- **JWT Authentication**: Secures the API endpoints.
- **Order Placement**: Allows authenticated clients to place orders.
- **Order Cancellation**: Orders can be canceled if they are in the `NEW` state.
- **Order History**: Clients can fetch their order history with pagination support.
- **Order Dispatch**: Automatically dispatches new orders via a scheduled job.
  
## Tech Stack
- **Java 17**
- **Spring Boot**: (Data JPA, Web, Security, Validation)
- **MySQL**: For persistent storage of client and order data.
- **JWT**: For securing API endpoints.
- **Docker & Docker Compose**: For containerization and running the application along with MySQL.

## Dependencies
Key dependencies used in this project:

- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Lombok
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)
- MySQL Connector

## Project Structure
- `AuthenticationService`: Handles client signup and login.
- `OrderService`: Handles order placement, cancellation, and fetching order history.
- `OrderDispatchJob`: A scheduled job to dispatch new orders.
- `JwtService`: Handles JWT generation and validation.
  
## Endpoints
### Authentication
- `POST /auth/signup`: Register a new client.
- `POST /auth/login`: Log in an existing client.

### Orders
- `POST /api/orders/place`: Place a new order (requires authentication).
- `PATCH /api/orders/cancel/{referenceNumber}`: Cancel an order (requires authentication).
- `GET /api/orders/history`: Fetch the order history with pagination (requires authentication).

## Setup and Installation

### Prerequisites
- **Java 17**
- **Maven**
- **Docker**
  
### Steps to Run the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Bhanuja1999/order_management_api.git
   cd order_management_api
   ```

2. **Environment Variables**: The application expects the following environment variables to be set:

- `SPRING_DATASOURCE_URL`: Database connection URL.
- `SPRING_DATASOURCE_USERNAME`: Database username.
- `SPRING_DATASOURCE_PASSWORD`: Database password.
- `JWT_SECRET_KEY`: Secret key for signing JWTs.
- `JWT_EXPIRATION_TIME`: JWT expiration time.

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application with Docker**: The application is already dockerized, and you can run it using Docker Compose:
   ```bash
   docker-compose up --build
   ```

### Application Access

- The application runs on `http://localhost:8080`.
- MySQL is accessible on `localhost:3306` with the database name `order_management_db`.


