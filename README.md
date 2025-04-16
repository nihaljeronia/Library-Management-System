
# 📚 Library Management System (LMS)

A full-stack Library Management System developed using **Spring Boot**, **MySQL**, and a **hybrid persistence approach** leveraging in-memory `HashMap` for temporary staging and database storage for permanence.

---

## 🚀 Overview

This project provides a user-friendly interface for students and librarians to manage books, borrowing records, and approvals. Features include:
- Role-based access for Admin and Users
- Temporary book/user staging using HashMap
- Full borrow/return system with tracking
- Clean, responsive frontend

---

## 🛠️ Tech Stack

| Layer      | Technology        |
|------------|-------------------|
| Backend    | Java, Spring Boot |
| Frontend   | HTML, CSS, JS     |
| Database   | MySQL             |
| Tools      | Postman, VS Code  |

---

## ✨ Features

- ✅ User Registration with Approval Flow
- ✅ Role-based Login & Dashboards
- ✅ Add/Edit/Delete/Search Books
- ✅ Temporary Book/User Staging (HashMap)
- ✅ Borrow/Return System with History

---

## 🧪 API Endpoints (Sample)

| Method | Endpoint                  | Description                 |
|--------|---------------------------|-----------------------------|
| POST   | `/login`                  | User login                  |
| POST   | `/books/temp`             | Add book to temp storage    |
| POST   | `/books/save`             | Save temp books to DB       |
| PUT    | `/users/approve/{id}`     | Admin approves user         |
| POST   | `/borrow`                 | Borrow a book               |
| PUT    | `/borrow/return/{id}`     | Return a borrowed book      |

---

## 🧰 How to Run the Project

1. **Clone the Repository**
```bash
git clone https://github.com/nihaljeronia/Library-Management-System.git
cd Library-Management-System
```

2. **Set Up MySQL Database**
- Create a database named `lms`
- Use the SQL script below to generate required tables

3. **Configure Database**
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lms
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **Run the Application**
```bash
./mvnw spring-boot:run
```
Or open the project in VS Code/IDEA and run `LmsApplication.java`

5. **Access the Application**
```
http://localhost:8080/login.html
```

---

## 🧩 SQL Script (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS lms;
USE lms;

CREATE TABLE IF NOT EXISTS user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  role VARCHAR(50),
  status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS book (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  isbn VARCHAR(50),
  title VARCHAR(255),
  author VARCHAR(255),
  genre VARCHAR(100),
  total_copies INT,
  available_copies INT
);

CREATE TABLE IF NOT EXISTS borrow_record (
  record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  borrow_date DATETIME,
  return_date DATETIME,
  returned BOOLEAN,
  book_id BIGINT,
  user_id BIGINT,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (user_id) REFERENCES user(id)
);
```

---

## 👨‍💻 Author
**Nihal Jeronia (Meeehhhh)**  

## 📌 License

This project is open-source and available for academic and learning use.
