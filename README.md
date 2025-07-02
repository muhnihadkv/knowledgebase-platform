# 📚 Knowledge Base Platform  

A web-based Knowledge Base Platform that allows teams to collaboratively create, edit, organize, and search documents with privacy controls, user mentions, and version tracking. This project is built using Java Spring Boot as a monolithic application.

## 🚀 Features

* ✅ User Authentication

  * User Registration with Email
  * Login with Email & Password
  * Forgot Password with Email Reset
  * JWT-based API Authentication

* ✅ Document Management

  * Document Listing with Metadata (Title, Author, Last Modified, Visibility)
  * Document Creation
  * Document Editing with Auto-save Support
  * Global Search by Document Title and Content (MySQL Full-Text Search)

* ✅ User Collaboration

  * User Mentions: @username triggers notifications and auto-sharing
  * Auto-sharing: Mentioned users automatically get view access to the document

* ✅ Privacy Controls

  * Public Documents: Accessible to anyone with the link
  * Private Documents: Restricted to the author and shared users
  * Permission Management: Share documents with View/Edit roles

* ✅ Version Control

  * Tracks Document Changes with Timestamps
  * View Previous Versions
  * Shows Who Edited and When
  * Version History Retrieval

* ✅ Built with:

  * Spring Boot
  * JWT Authentication
  * MySQL Database
  * Docker and Docker Compose for Containerization

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot
* Spring Security
* MySQL 8
* Docker
* Maven

---



## ⚙️ Configuration

Before running the project, update the following properties in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/knowledgebase
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# JWT Secret
jwt.secret=your_secret_key

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password_or_app_password

```

Make sure your MySQL database is running and the schema is created.

---

## 🐳 Running the Project with Docker

1. Clone the repository:

```bash
git clone https://github.com/muhnihadkv/knowledgebase-platform.git
cd knowledgebase-platform
cd Backend
```

2. Build and run using Docker Compose:

```bash
docker-compose up --build
```

3. The backend will be available at:

```http
http://localhost:8080
```
