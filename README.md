Blog application using Spring Boot, Thymeleaf and Spring Data JPA: CRUD

(This project contains pagination, search, filtering and sorting)

In project we make is a basic blog with features of posts and comments. We are skipping authentication for now. However all unauthorized actions or use cases below will be replaced with authentication in next project using spring boot.

This project uses following technologies

Spring MVC as MVC framework
Spring Data JPA for Database Connectivity
Thymeleaf for theming and templates
Use Cases:
Non logged in user should be able to read a list of blog posts that has title, excerpt, author, published DateTime and tags.
Non logged in user should be able to read full blog post that has title, content, author, published DateTime and tags.
Non logged in user should be able to filter blog posts using author, published DateTime and tags
Non logged in user should be able to sort blog posts using the published DateTime
Non logged in user should be able to search blog posts using full text search on title, content, author and tags
Non logged in user should be able to navigate to different pages on blog list where each page lists maximum 10 blog posts.
Non logged in user should be able to create blog post that has title, content, author, published DateTime and tags
Non logged in user should be able to update a blog post that has title, content, author, published DateTime and tags
Non logged in user should be able to delete a blog post that has title, content, author, published DateTime and tags
Non logged in user should be able to comment on blog posts using comment form that contains name, email and comment
Non logged in user should be able to read, update and delete comments.

Blog application using Spring Boot, Spring Data JPA, Spring Security: Authentication & Authorization

Thymeleaf
pagination
search
filtering
sorting
Authentication and Authorization
