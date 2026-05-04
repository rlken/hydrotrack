# 💧 HydroTrack

**Water Fountain Cleanliness Monitoring System**
National University Manila · Advanced DBMS Course Project · SDG 6: Clean Water and Sanitation

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### 1. Clone the repository
```bash
git clone https://github.com/YOUR-ORG/hydrotrack.git
cd hydrotrack
```

### 2. Set up the database
```sql
CREATE DATABASE hydrotrack;
```

Then run the advanced DB features:
```bash
mysql -u root -p hydrotrack < sql/views.sql
mysql -u root -p hydrotrack < sql/procedures.sql
mysql -u root -p hydrotrack < sql/triggers.sql
```

### 3. Configure database credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### 4. Run the application
```bash
./mvnw spring-boot:run
```
Open: **http://localhost:8080**

### 5. Default login
| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Admin |
| `tech.juan` | `password123` | Technician |
| `insp.maria` | `password123` | Inspector |
| `viewer.jose` | `password123` | Viewer |

---

## 📁 Project Structure

```
hydrotrack/
├── pom.xml                          # Maven dependencies
├── sql/                             # Advanced DB features
│   ├── views.sql                    # 5 database views
│   ├── procedures.sql               # 3 stored procedures + 2 functions
│   └── triggers.sql                 # 5 triggers
├── src/main/
│   ├── java/com/hydrotrack/
│   │   ├── config/                  # Security config (RBAC)
│   │   ├── controller/              # Web controllers
│   │   ├── model/                   # JPA entities
│   │   ├── repository/              # Data access layer
│   │   └── service/                 # Business logic
│   └── resources/
│       ├── schema.sql               # DDL + indexes
│       ├── data.sql                 # Seed data
│       ├── static/css/style.css     # Apple-style UI
│       └── templates/               # Thymeleaf views
```

---

## 👥 Team Members & Responsibilities

| Member | Module |
|---|---|
| Member 1 | Database Design (schema, indexes, views, procedures, triggers) |
| Member 2 | Fountain & Building modules |
| Member 3 | Water Quality & Maintenance modules |
| Member 4 | Frontend UI (templates, CSS, JS) |
| Member 5 | Security, Dashboard, Auth, Integration |

---

## 🔧 Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Vanilla CSS (Apple-inspired), JavaScript
- **Database**: MySQL 8 (InnoDB)
- **Build**: Maven

---

## 🗄️ Advanced Database Features

| Feature | Files |
|---|---|
| B-tree & Hash Indexes | `schema.sql` |
| 5 Views | `sql/views.sql` |
| 3 Stored Procedures + 2 Functions | `sql/procedures.sql` |
| 5 Triggers (audit, validation, automation) | `sql/triggers.sql` |
| ACID Transactions | `MaintenanceService.java` |
| Role-Based Access Control | `SecurityConfig.java` |
