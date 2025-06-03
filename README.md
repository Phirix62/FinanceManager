# FinFlow – Backend (Spring Boot)

FinFlow is a personal wealth manager web application. This repository contains the backend REST API built with **Java Spring Boot**, **Spring Data JPA**, and **MySQL**.

---

## 🚀 Features

- User registration & login (with password encryption)
- Income & expense tracking by user
- Dashboard statistics & chart data
- RESTful API design
- JPA entities & DTO separation
- Unit tests for service and controller layers
- JavaDoc documentation

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL
- Maven
- JUnit / Mockito
- Lombok

---

## 🔧 How to Run

1. **Clone the repo**
```bash
git clone https://github.com/<your-username>/FinanceManager.git
cd FinanceManager
```

2. Configure **application.properties**
```text
spring.datasource.url=jdbc:mysql://localhost:3306/finflow
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

4. Run with Maven or in IDE
mvn spring-boot:run

## 📚 Documentation
JavaDoc is available in /docs or see GitHub Page : [Backend doc](https://phirix62.github.io/FinanceManager/)
API Reference available in the project document

## 📂 Project Structure
```sql
com.FinFlow.FinanceManager
├── controller/
├── dto/
├── entity/
├── repository/
├── services/
│   ├── expense/
│   ├── income/
│   ├── user/
│   └── stats/
└── FinanceManagerApplication.java
```

## ✅ Tests
```bash
mvn test
```

## 🧑‍💻 Authors
[@Phirix62](https://github.com/Phirix62)
