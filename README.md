# Safe Flow

**Safe Flow** is a metro safety and route support web application built for urban travelers.
It combines route exploration, ticket-oriented flows, traveler reporting, and admin moderation in a single Java web platform.

The project focuses on a simple idea:

> make metropolitan travel easier to navigate and safer to report

## Overview

Safe Flow lets users:

- explore supported metro networks
- search routes between stations
- purchase and manage simulated metro tickets
- access a reserved traveler area
- submit safety reports tied to a city
- classify reports through alert badges such as `Pickpocket alert`, `Fight alert`, `Crowd alert`, and `General alert`
- receive internal notification outcomes after admin moderation

Administrators can:

- review pending traveler reports
- approve or reject reports
- publish platform-wide communications
- create or remove admin accounts
- manage traveler accounts
- monitor saved route data

## Supported Cities

The application currently supports the metro networks implemented in the routing engine:

- Rome
- Naples
- Athens
- Budapest

## Main Features

### Traveler Experience

- route search between origin and destination stations
- metro map preview by city
- ticket purchase flow
- reserved area with saved routes and purchased tickets
- internal notifications for report approval outcomes
- traveler registration

### Safety Reporting

- city-based reporting
- report classification through alert badges
- station confirmation with autocomplete
- moderation workflow before public visibility
- city-filtered public report feed

### Admin Experience

- admin hub with notification badge for pending traveler reports
- moderation queue with approval and rejection reasons
- admin-to-platform broadcast messages
- admin account creation and deletion
- traveler account overview and removal

## Tech Stack

- Java 17
- Maven
- JSP + Servlet API
- JSTL
- Tomcat-compatible WAR packaging
- MySQL Connector/J
- MariaDB/MySQL database
- SLF4J + Logback

## Project Structure

```text
src/main/java        application logic, controllers, DAO layer, models
src/main/webapp      JSP pages, static assets, WEB-INF configuration
Database/            schema, stored procedures, grants
FileLog/             application logs
```

## Architecture

The project follows a layered MVC-style structure:

- `controller/grafico`: servlet entry points and web flow
- `controller/applicativo`: application-level orchestration
- `dao`: database persistence and stored procedure integration
- `model` / `bean` / `record`: domain and transport structures
- `WEB-INF/views`: protected JSP views

Sensitive pages are served through controllers and protected under `WEB-INF/views`.

## Database

The application is designed to run in **full database mode only**.
There is no remaining demo persistence layer.

Relevant files:

- [Database/RouteX_Update.sql](Database/RouteX_Update.sql)
- [Database/RouteX_users_grants.sql](Database/RouteX_users_grants.sql)

Default database connection properties are defined in:

- [src/main/resources/db.properties](src/main/resources/db.properties)

## Getting Started

### 1. Create and populate the database

Run the SQL files in this order:

```sql
source /absolute/path/to/Database/RouteX_Update.sql
source /absolute/path/to/Database/RouteX_users_grants.sql
```

### 2. Check database credentials

Update `src/main/resources/db.properties` if your local database uses different credentials or host settings.

### 3. Build the project

```bash
mvn clean compile
```

### 4. Deploy the WAR

Build and deploy the application on a servlet container such as Apache Tomcat.

Example:

```bash
mvn package
```

Then deploy the generated WAR file from `target/`.

## Authentication Roles

The platform currently supports:

- `TRAVELER`
- `ADMIN`

The legacy `WORKER` role has been removed from both application logic and database bootstrap.

## Reporting Model

Traveler reports are not published immediately.

The flow is:

1. a traveler submits a report
2. the report enters the admin moderation queue
3. an admin approves or rejects it
4. the traveler receives an internal notification with the outcome
5. approved reports appear in the public notifications feed

## Notes

- ticket purchase is currently simulated inside the platform
- only supported cities should be used in route and reporting flows
- if you update the SQL schema, redeploy the application after reloading the database

## Authors

Developed by:

- Lorenzo Brondi
- Simone Remoli

Concept and product vision originally conceived by **Simone Remoli**,
Computer Engineering student at the University of Rome Tor Vergata.
