Let's Go!

A RESTful multi-module web-service for posting and sharing events and find companions to participate in them.

The application serves as an event listing platform where users can propose events and gather participants.

Technology Stack: Java, Spring Boot, SQL, PostgreSQL, JPA, Hibernate, Maven, Lombok, GoogleMap API, Docker.

Microservices

The main service contains all the necessary functionality for the product.

The API is divided into three parts: The first part is public and accessible to any network user without registration. The second part is restricted and accessible only to authenticated users. The third part is administrative, intended for service administrators.

Statistics Service stores the number of views and enables various queries for analyzing the application's performance.

I also plan to add Spring Security and front-end to this project.

How to Use: mvn clean package docker-compose up -d