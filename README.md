# LogDB: Centralized Log Analytics Platform

LogDB is a full-stack web application built with **Spring Boot** to provide a centralized interface for storing, managing, and analyzing **HDFS** and **Web server logs**. The platform offers a secure environment for executing complex analytical queries via a **PostgreSQL** backend and a **Thymeleaf** web interface.

---

## Technical Overview

The platform is designed around a layered architecture (**API Layer - Service Layer - Data Access Layer**) to ensure clear separation of concerns.

* **Backend Framework**: Developed using **Spring Boot**, providing a front-end API for core application operations.
* **Database Engine**: Built on **PostgreSQL**, utilizing specialized data types like `timestamp with time zone` and `numeric` for precise log metrics.
* **Frontend & Templating**: Uses **Thymeleaf** for server-side rendering, featuring a modular system of fragments and layouts to manage the web interface.
* **Security Infrastructure**: Utilizes **Spring Security** for role-based access control and supports both standard web login and **JWT (JSON Web Tokens)**.

---

## Project Structure

The project follows a feature-oriented package structure for scalability:

```text
src/main/java/com/example/LogDB/
├── authentication/   # Login/Signup logic
├── email/            # SMTP Service implementation
├── exception/        # Global Exception Handling
├── jwt/              # JWT filters and Token Whitelisting
├── logs/             # HDFS & Web log entities and repositories
├── registration/     # User registration & Email tokens
├── security/         # Security filters & Password encoding
├── users/            # User RBAC (Role-Based Access Control)
└── web/              # Main WebController for UI routing

```

---

## Analytical Queries

The system features fifteen pre-defined analytical queries materialized as **PostgreSQL stored functions** to ensure high performance.

* **Volume & Trends**: Track total logs per type or day within specific time ranges.
* **Traffic Analysis**: Identify the top source IPs for any given day.
* **HDFS Block Metrics**: Monitor top-5 Block IDs by activity and identify blocks replicated and served within the same day or hour.
* **Web Resource Analytics**: Find referers linked to multiple resources and identify the second-most requested resource.
* **Client & Filtering**: Extract logs based on response size or specific browser versions (e.g., Firefox).
* **HTTP Method Tracking**: Filter IPs by the use of one, two, or four distinct HTTP methods within a time range.
* **Direct Lookups**: Retrieve full HDFS or Web log records by their unique IDs.

---

## Features

### Log Management & Analytics

* **Log Data Schema**: Processes common attributes in a base `logs` relation, with specialized attributes for Web and HDFS logs stored in extended relations.
* **Analytics Queries**: Supports fourteen specific analytical queries, ranging from IP-based searches to log type frequency analysis, materialized via stored procedures.
* **CRUD Operations**: Authenticated users can insert new log entries, search by IP, and delete or modify records with a parallel projection of current values for editing.
* **Query Optimization**: Utilizes composite **PostgreSQL** indexes on `time_date` and `blockid` to accelerate heavy analytical computations.

### Security & Identity

* **Authentication**: Supports standard browser login through a dedicated login page and programmatic access via JWT tokens.
* **JWT Management**: Handles token generation and refreshing; refreshing a token automatically revokes previous valid tokens to enhance security.
* **User Registration**: New users are verified through an email-based token system and must activate their account within five minutes.

---

## Database Schema (PostgreSQL)

The database consists of six relations: three for log data and three for user management and authentication.

```sql
create table logs (
    log_id serial,
    time_date timestamp with time zone not null,
    source_ip varchar(15),
    log_type varchar(30),
    specific_type varchar(30),
    blockid numeric(20),
    primary key (log_id)
);

create table web_logs (
    log_id integer,
    size numeric(5),
    username varchar(15),
    user_id varchar(15),
    http_status numeric(3),
    response_size numeric(5),
    referers varchar(140),
    resources varchar(140),
    agent varchar(40),
    primary key (log_id),
    foreign key (log_id) references logs on delete cascade
);

create table hdfs_logs (
    log_id integer,
    destination_ip varchar(50),
    size numeric(10),
    primary key (log_id),
    foreign key (log_id) references logs on delete cascade
);

create table users (
    user_id serial,
    first_name varchar(25) not null,
    last_name varchar(25) not null,
    user_name varchar(25) not null unique,
    mail varchar(255) not null unique,
    password varchar(72) not null,
    role varchar(25) not null,
    locked boolean not null default false,
    enabled boolean not null default false,
    primary key (user_id)
);

create table confirmtoken (
    token_id serial,
    token varchar(36) not null,
    created timestamp with time zone not null,
    expires timestamp with time zone not null,
    confirmed timestamp with time zone,
    user_id integer not null,
    primary key (token_id),
    foreign key (user_id) references users
);

create table jwttoken (
    token_id serial,
    token varchar(200) not null,
    tokenType varchar(20) not null,
    revoked boolean not null default false,
    expired boolean not null default true,
    user_id integer not null,
    primary key (token_id),
    foreign key (user_id) references users
);

```

---

## Building and Running

The project is managed with **Maven**.

**1. Prerequisites**

* **JDK 17+**
* **PostgreSQL** instance
* **SMTP Server** access for registration emails

**2. Compilation**

```bash
./mvnw clean package

```

**3. Execution**

```bash
./mvnw spring-boot:run

```

Access the application at `http://localhost:8080`.

---

## License

This project is licensed under the MIT License.