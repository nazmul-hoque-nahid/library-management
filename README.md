# 📚 Library Management System

A backend-focused **Library Management System** built with **Spring Boot** and **MySQL**, providing secure REST APIs for managing books, users, borrowing, book copies, fines, authors, publishers, and categories.

The application is containerized using **Docker** and deployed to **Microsoft Azure**. A **GitHub Actions CI/CD pipeline** automatically builds and tests the project, creates a Docker image, and pushes the image to Docker Hub for deployment.

---
## 🌐 Live Demo

[![Swagger API](https://img.shields.io/badge/Swagger-Live%20API-green?logo=swagger)](https://library.blueglacier-6fbbfd1c.eastasia.azurecontainerapps.io/swagger-ui/index.html)
## 🚀 Features

### 🔐 Authentication & Authorization

* User registration and login
* JWT-based authentication
* Stateless authentication using Spring Security
* Role-based authorization
* Password change
* Protected REST endpoints
* Custom authentication and access-denied handling

### 👤 User Management

* Create and manage users
* Activate/suspend users
* Change user roles
* Search users
* View user information

### 📚 Book Management

* Create and manage books
* Search books
* Manage authors, categories, and publishers
* Track total and available copies

### 📦 Book Copy Management

* Manage individual physical book copies
* Track copy status:

    * AVAILABLE
    * BORROWED
    * RESERVED
    * LOST
    * DAMAGED
* Search book copies

### 🔄 Borrowing Management

* Issue books to users
* Return books
* Track borrowing history
* Find overdue books
* Search borrowing records

### 💰 Fine Management

* Create fines
* View fines
* Search fines
* Pay fines
* Waive fines

### 🛡️ Error Handling & Validation

* Global exception handling
* Custom exceptions
* Structured API error responses
* Request validation

### 🔎 Search & Filtering

The application provides search/filtering functionality for resources such as:

* Books
* Users
* Authors
* Book copies
* Borrow records
* Fines

---

## 🏗️ Architecture

The project follows a layered Spring Boot architecture:

```text
Client
   │
   ▼
REST Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
JPA / Hibernate
   │
   ▼
MySQL
```

Security flow:

```text
Client
   │
   │ Authorization: Bearer <JWT>
   ▼
JWT Authentication Filter
   │
   ▼
Spring Security
   │
   ▼
Controller
   │
   ▼
Service
```

---

## 🛠️ Technologies

| Technology               | Purpose                        |
| ------------------------ | ------------------------------ |
| Java                     | Programming language           |
| Spring Boot              | Backend framework              |
| Spring Security          | Authentication & authorization |
| JWT                      | Stateless authentication       |
| Spring Data JPA          | Data access                    |
| Hibernate                | ORM                            |
| MySQL                    | Relational database            |
| Maven                    | Build & dependency management  |
| Docker                   | Application containerization   |
| GitHub Actions           | CI/CD                          |
| Docker Hub               | Container image registry       |
| Microsoft Azure          | Cloud deployment               |
| Azure Database for MySQL | Cloud database                 |
| OpenAPI / Swagger        | API documentation              |

---

## 📂 Project Structure

```text
src/main/java/
└── ...
    ├── config/
    │   ├── SecurityConfig
    │   ├── CorsConfig
    │   └── OpenApiConfig
    │
    ├── controller/
    │   ├── AuthController
    │   ├── UserController
    │   ├── BookController
    │   ├── BookCopyController
    │   ├── BorrowRecordController
    │   ├── FineController
    │   ├── AuthorController
    │   ├── PublisherController
    │   └── CategoryController
    │
    ├── service/
    │   ├── AuthService
    │   ├── UserService
    │   ├── BookService
    │   ├── BookCopyService
    │   ├── BorrowRecordService
    │   ├── FineService
    │   ├── AuthorService
    │   ├── PublisherService
    │   └── CategoryService
    │
    ├── repository/
    │
    ├── entity/
    │
    ├── dto/
    │
    ├── security/
    │
    ├── exception/
    │
    └── specification/
```

---

## 🔑 API Endpoints

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
PUT  /api/auth/change-password
```

### Books

```text
POST   /api/books
GET    /api/books
GET    /api/books/{id}
GET    /api/books/search
DELETE /api/books/{id}
```

### Book Copies

```text
POST   /api/book-copies
GET    /api/book-copies
GET    /api/book-copies/{id}
GET    /api/book-copies/{bookId}/copies
GET    /api/book-copies/search
PUT    /api/book-copies/{id}
DELETE /api/book-copies/{id}
```

### Borrowing

```text
POST /api/borrow-records
GET  /api/borrow-records
GET  /api/borrow-records/{id}
PUT  /api/borrow-records/{id}/return
GET  /api/borrow-records/{userId}/borrows
GET  /api/borrow-records/book-copy/{copyId}
GET  /api/borrow-records/search
GET  /api/borrow-records/overdue
```

### Users

```text
GET /api/users
GET /api/users/{id}
GET /api/users/search

PUT /api/users/{id}/activate
PUT /api/users/{id}/suspend
PUT /api/users/{id}/change-role
PUT /api/users/{id}/update
```

### Fines

```text
POST /api/fines
GET  /api/fines
GET  /api/fines/{id}
GET  /api/fines/user/{userId}
GET  /api/fines/search

PUT /api/fines/{id}/pay
PUT /api/fines/{id}/waive
DELETE /api/fines/{id}
```

Similar REST APIs are provided for authors, publishers, and categories.

---

# 🐳 Docker

The Spring Boot application is packaged as a Docker image.

Basic deployment flow:

```text
Spring Boot Application
        │
        ▼
      Docker
        │
        ▼
   Docker Image
        │
        ▼
    Docker Hub
        │
        ▼
      Azure
```

The application container connects to a **MySQL database hosted on Azure**.

---

# ⚙️ CI/CD with GitHub Actions

This project uses **GitHub Actions** to automate the build and container image workflow.

```text
Developer pushes code
        │
        ▼
   GitHub Repository
        │
        ▼
 GitHub Actions
        │
        ├── Checkout source code
        │
        ├── Setup Java
        │
        ├── Build project
        │
        ├── Run tests
        │
        ├── Build Docker image
        │
        └── Push image to Docker Hub
                    │
                    ▼
             Docker Hub
                    │
                    ▼
                 Azure
```

This means a new Docker image can be produced automatically whenever changes are pushed to the configured branch.

---

# ☁️ Azure Deployment

The application is deployed to **Microsoft Azure** as a containerized Spring Boot application.

### Cloud Architecture

```text
                    ┌─────────────────────┐
                    │   GitHub Repository  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │  GitHub Actions CI  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Docker Hub      │
                    │   Container Image   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Azure Container   │
                    │      Application    │
                    └──────────┬──────────┘
                               │
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Azure Database for  │
                    │       MySQL         │
                    └─────────────────────┘
```

### Deployment Components

* **Application:** Spring Boot
* **Containerization:** Docker
* **Container Registry:** Docker Hub
* **Cloud Platform:** Microsoft Azure
* **Database:** Azure Database for MySQL
* **CI/CD:** GitHub Actions

---

# 🗄️ Database

The application uses MySQL as its relational database.

Main entities include:

```text
User
Book
BookCopy
Author
Category
Publisher
BorrowRecord
Fine
```

Important relationships include:

```text
Book ──────── BookCopy
 │
 ├────────── Author
 │
 ├────────── Category
 │
 └────────── Publisher

User ──────── BorrowRecord
                    │
                    ▼
                BookCopy

BorrowRecord ────── Fine
```

For production deployment, the application connects to **Azure-hosted MySQL** rather than a locally running database.

---

# 🔐 Security

Spring Security and JWT are used to secure the REST API.

Authentication flow:

```text
1. User logs in
       ↓
2. Server authenticates credentials
       ↓
3. Server generates JWT
       ↓
4. Client stores JWT
       ↓
5. Client sends JWT with requests
       ↓
6. JWT Filter validates token
       ↓
7. Spring Security checks roles
       ↓
8. Request reaches controller
```

Example:

```http
Authorization: Bearer <JWT_TOKEN>
```

Role-based access control is implemented using Spring Security.

---

# ▶️ Running Locally

## Prerequisites

* Java 26
* Maven
* MySQL
* Docker (optional)

## 1. Clone the repository

```bash
git clone https://github.com/nazmul-hoque-nahid/library-management
```

## 2. Configure MySQL

Create a MySQL database and configure the application properties.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_management
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real database credentials to GitHub.

## 3. Build the project

```bash
mvn clean package
```

## 4. Run the application

```bash
mvn spring-boot:run
```

The API will normally be available at:

```text
http://localhost:8080
```

---

# 📖 API Documentation

The project includes OpenAPI configuration for API documentation.

When running locally, access the Swagger/OpenAPI UI at the configured Swagger endpoint.

---

# 🧪 Testing

The project can be built and tested through Maven:

```bash
mvn clean test
```

The same build/test process is integrated into the GitHub Actions workflow.

---

# 🔄 Development & Deployment Workflow

```text
        Developer
            │
            ▼
       Write Code
            │
            ▼
     Push to GitHub
            │
            ▼
   GitHub Actions
            │
       ┌────┴────┐
       ▼         ▼
    Build       Test
       │         │
       └────┬────┘
            ▼
     Build Docker Image
            │
            ▼
       Push to Docker Hub
            │
            ▼
     Deploy Container
         to Azure
            │
            ▼
     Azure MySQL Database
```

---

# 🎯 What I Learned

Through this project, I practiced:

* Building RESTful APIs with Spring Boot
* Layered backend architecture
* Spring Data JPA and Hibernate
* Entity relationships
* DTO-based API design
* Spring Security
* JWT authentication
* Role-based authorization
* Exception handling
* API validation
* Search and filtering using specifications
* Docker containerization
* GitHub Actions CI/CD
* Docker image publishing
* Cloud deployment using Microsoft Azure
* Connecting a containerized application to Azure MySQL

---


# 👨‍💻 Author

**Nahid**

Software Engineering Student | Backend Developer

### Technologies

`Java` `Spring Boot` `Spring Security` `JWT` `JPA` `Hibernate` `MySQL` `Docker` `GitHub Actions` `Azure`































