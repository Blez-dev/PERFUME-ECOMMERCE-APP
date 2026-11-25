# E-Commerce Backend API

A production-style RESTful e-commerce backend built with **Java and Spring Boot**. The application provides secure authentication, role-based authorization, product and category management, shopping cart functionality, order processing, input validation, exception handling, and Docker support.

The project is designed to demonstrate practical backend engineering skills using the Spring ecosystem, with a focus on clean API design, data persistence, security, and maintainability.

## Technologies

- **Language:** Java 21
- **Framework:** Spring Boot
- **Web:** Spring Web MVC
- **Data Access:** Spring Data JPA, Hibernate
- **Security:** Spring Security, JWT Authentication, BCrypt
- **Database:** PostgreSQL 18
- **Messaging:** RabbitMQ
- **Validation:** Jakarta Bean Validation
- **Build Tool:** Maven
- **Testing:** JUnit 5, Mockito
- **Containerization:** Docker, Docker Compose
- **API Documentation:** Swagger / OpenAPI
- **Version Control:** Git, GitHub

## Features

### Authentication & Authorization
- User registration and login
- Password hashing with BCrypt
- JWT-based authentication
- Role-based access control
- Support for `CUSTOMER` and `ADMIN` roles
- Protected endpoints using Spring Security

### Product Management
- Create, retrieve, update, and delete perfume products
- Product categorization
- Category management

### Shopping Cart
- Create and manage user shopping carts
- Add and remove cart items
- Update item quantities
- Automatic cart total calculations

### Orders & Checkout
- Checkout using items in the shopping cart
- Order creation and management
- Order and order-item management
- Order status updates
- Order cancellation
- User-specific order access and authorization

### API Areas
The backend provides REST endpoints for the following resources:

- Authentication
- Users
- Roles
- Products
- Categories
- Shopping Cart
- Cart Items
- Orders
- Order Items
- Inventory

## API Documentation

The API is documented using **Swagger / OpenAPI**.

When the application is running, the available endpoints, request parameters, and response schemas can be explored through the Swagger UI.

## Project Goals

This project demonstrates practical backend development concepts, including:

- RESTful API design and development
- Relational database design and persistence
- Authentication and authorization
- Business logic implementation
- Entity relationships and ORM
- Input validation
- Centralized exception handling
- Automated testing
- Application containerization
- API documentation
- Production-oriented configuration

## Author

**Adu Ibiowotisi Blessing**  
Java Backend Developer

[LinkedIn](https://www.linkedin.com/in/blessing-adu/)


