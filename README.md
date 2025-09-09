# **Cooking Menu Application**

**Cooking Menu** is a Spring Boot **REST API** that enables companies to manage daily lunch menus and track employee attendance to them. The system is designed with **role-based access control**, ensuring that users (Employees), cooks, and administrators have access to the appropriate features.

This project demonstrates practical **Spring Boot development skills**, including **JPA/Hibernate, Spring Security with JWT, REST API design**, and **database relationships**.

### Key Features

* **Menu Management**
  * Create, update and view daily lunch menus
  * Support for multiple dishes per menu
  * Attendance


* **Ingredient & Dish Management**
  * Comprehensive ingredient database
  * Track dish composition and fasting properties


* **Attendance System**
  * Employees can register/cancel lunch attendance
  * Attendance tracking with time restrictions


* **Fasting-Friendly Indicators**
  * Automatic detection of fasting-suitable meals based on ingredients


* **Role-Based Access Control**
  * User – View menus, register/cancel attendance
  * Cook – Manage dishes, ingredients, and menus
  * Admin – Full access, including user management


* **JWT Authentication**
  * Secure login and token-based authentication
  * Protects endpoints based on roles

### Tech Stack

#### Backend

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security** - Authentication & Authorization
* **Spring Data JPA** - Database operations
* **Hibernate** - ORM framework
* **JWT (JSON Web Tokens)** - Stateless authentication

#### Database
* **SQL Server** - Primary database
* **JPA/Hibernate** - Database abstraction layer

#### Development Tools

* **Maven** - Dependency management
* **Lombok** - Boilerplate code reduction
* **Jakarta Validation** - Input validation
* **JUnit 5** - Unit testing
* **Mockito** - Mocking framework

#### Layered Architecture
```text 
┌─────────────────┐
│   Controllers   │ ← REST endpoints, request handling
├─────────────────┤
│    Services     │ ← Business logic, transactions
├─────────────────┤
│    Managers     │ ← DTO/Entity mapping, data transformation
├─────────────────┤
│  Repositories   │ ← Data access layer
├─────────────────┤
│   Entities      │ ← JPA entities, database mapping
└─────────────────┘
```

#### API Endpoints
* Authentication
```text
POST /auth/register    # User registration
POST /auth/login       # User authentication
```
* Ingredients (Cooks/Admins only)
```text
GET    /ingredients           # Get all ingredients
POST   /ingredients           # Add new ingredient
PUT    /ingredients/update    # Update ingredient
DELETE /ingredients/delete/{name}  # Delete ingredient
```
* Dishes (Cooks/Admins for modifications, All users for viewing)
```text
GET    /dishes               # Get all dishes
POST   /dishes               # Add new dish
PUT    /dishes/update        # Update dish
DELETE /dishes/delete/{name} # Delete dish
```
* Menus 
```text
GET    /menus                    # Get future menus (All users)
GET    /menus/all                # Get all menus (Cook/Admin)
GET    /menus/{dishName}         # Get menus by dish (Cook/Admin)
POST   /menus                    # Create menu (Cook/Admin)
PUT    /menus/update             # Update menu (Cook/Admin)
DELETE /menus/delete/{date}      # Delete menu (Cook/Admin)
POST   /menus/attend             # Register/cancel attendance (All users)
```

### Installation & Setup

#### Prerequisites

* Java 17 or higher
* SQL Server
* Maven 3.6+

#### Environment Setup
1. Clone the repository
    ```text
    git clone https://github.com/IDekishaI/CookingMenu
    cd CookingMenu
   ```
2. Configure environment variables
    ```text
    export DB_USER=your_db_username
    export DB_PASS=your_db_password
    export JWT_SECRET=your_jwt_secret_key
   ```
3. Database setup 
   * Create a SQL Server database named CookingMenu 
   * Update connection settings in application.properties

4. Run the application
   ```text
   mvn spring-boot:run
   ```
### Authentication
#### JWT Implementation

* **Token Generation**: 1-hour expiration, HS256 algorithm
* **Request Flow**: Bearer token in Authorization header
* **Security Filter**: Custom JwtAuthFilter for request interception
* **User Details**: Custom UserService implementing UserDetailsService

#### Request Example
```text
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
### Testing
#### Test Coverage

* Unit Tests: Service layer with Mockito
* Integration Tests: Repository layer testing
* Validation Tests: Input validation scenarios

### API Response Format
#### Success Response
```json
{
  "name": "Tomato Salad",
  "dishIngredientDTOS": [
    {
      "ingredientName": "Tomato",
      "quantity": 2.0
    }
  ],
  "fastingSuitable": true
}
```
#### Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "name": "Dish name cannot be blank."
  },
  "timestamp": "2024-03-15T10:30:00"
}
```
### Developer Notes
This application demonstrates proficiency in:

* **Enterprise Java Development** with **Spring Boot** ecosystem
* **RESTful API Design** with proper HTTP semantics
* **Security Implementation** using JWT and role-based access
* **Database Design** with complex relationships and constraints
* **Clean Architecture** with separation of concerns
* **Test-Driven Development** with comprehensive unit tests
* **Production-Ready Code** with proper error handling and validation

The codebase follows Spring Boot best practices and demonstrates understanding of enterprise-level application development suitable for production environments.