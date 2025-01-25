# Conference Management System API

This project is a **RESTful API** built with **Spring Boot** that provides functionalities to manage scientific conferences. It allows users to manage conferences, submissions, evaluations, roles, and users. The project is integrated with Swagger UI for API documentation and testing.

---

## Features
- **Conference Management**: Create, update, view, and delete conferences.
- **Submission Handling**: Submit articles, view submissions, and manage submission states.
- **Evaluation Process**: Evaluate submissions with comments, states, and scores.
- **User Roles**: Assign roles to users for conferences (e.g., Organizer, Reviewer, Participant).
- **Swagger Documentation**: Built-in API documentation with Swagger UI.
- **Security**: Integrated with Spring Security for authentication and role-based authorization.
- **Database Integration**: Supports relational databases using Spring Data JPA and Hibernate.

---

## Prerequisites
- **Java**: JDK 17 or higher.
- **Maven**: Ensure Maven is installed and available in your environment.
- **Database**: Uses H2 in-memory database for development. Can be configured for MySQL, PostgreSQL, etc.
- **IDE**: IntelliJ IDEA, Eclipse, or any IDE of your choice.

---

## Technologies Used
- **Spring Boot**: Backend framework.
- **Spring Data JPA**: For database interactions.
- **Spring Security**: For authentication and authorization.
- **H2 Database**: Default in-memory database for development.
- **Swagger UI**: API testing and documentation.
- **Lombok**: To reduce boilerplate code.
- **ModelMapper**: For DTO-to-entity conversions.

---

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/yakoubham23/gestion-conferences-spring-boot.git 
cd conference-management-api



