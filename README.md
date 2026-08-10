<p align="center">
  <img src="src/main/webapp/images/safe-flow-logo.svg" alt="Safe Flow logo" width="760">
</p>

<p align="center">
  <img alt="Java 17" src="https://img.shields.io/badge/Java-17-0E7C66?style=for-the-badge">
  <img alt="MVC" src="https://img.shields.io/badge/Pattern-MVC-14241D?style=for-the-badge">
  <img alt="JSP Servlet" src="https://img.shields.io/badge/JSP%20%2B%20Servlets-Web%20CRM-405147?style=for-the-badge">
  <img alt="Tomcat" src="https://img.shields.io/badge/Tomcat-WAR-F59E0B?style=for-the-badge">
  <img alt="MySQL" src="https://img.shields.io/badge/MySQL-Persistence-1D4ED8?style=for-the-badge">
</p>

<p align="center">
  <img alt="Pickpocket alert" src="https://img.shields.io/badge/Pickpocket%20alert-High%20attention-B42318?style=flat-square">
  <img alt="Fight alert" src="https://img.shields.io/badge/Fight%20alert-Conflict-F59E0B?style=flat-square">
  <img alt="Crowd alert" src="https://img.shields.io/badge/Crowd%20alert-Density-0E7C66?style=flat-square">
  <img alt="General alert" src="https://img.shields.io/badge/General%20alert-Notice-405147?style=flat-square">
</p>

# Safe Flow

**Safe Flow** is a serious, focused CRM for reporting pickpocketing and criminal events on public transport. Travelers submit safety reports, administrators review them, and approved alerts become visible in a city-based notification feed.

## What It Does

- Travelers enter a reserved area centered on **System Alerts** and **Send Report**.
- Reports are classified with clear alert badges: pickpocket, fight, crowd, or general.
- Admins approve or reject traveler submissions before they become public.
- Every notification shows the author, avatar, role, and profile link.
- Users can manage a profile photo and bio from the avatar menu.

## Experience

Safe Flow is designed around fast situational awareness:

| Area | Purpose |
| --- | --- |
| **System Alerts** | City-filtered notifications for public transport incidents |
| **Send Report** | Traveler report flow with station and event details |
| **Moderation Queue** | Admin review before public visibility |
| **Profiles** | Avatar, bio, author identity, and `me` label for personal reports |

## Reporter Recognition

Safe Flow turns useful participation into visible trust signals. Travelers are ranked through a **community score** that rewards reliable, useful, and geographically aware reporting.

<p align="center">
  <img alt="Approved reports" src="https://img.shields.io/badge/Approved%20reports-%C3%9710-0E7C66?style=for-the-badge">
  <img alt="Helpful likes" src="https://img.shields.io/badge/Helpful%20likes-%C3%973-B42318?style=for-the-badge">
  <img alt="Covered cities" src="https://img.shields.io/badge/Covered%20cities-%C3%972-1D4ED8?style=for-the-badge">
  <img alt="Activity" src="https://img.shields.io/badge/Report%20activity-max%2010-F59E0B?style=for-the-badge">
</p>

```text
Community Score =
  (Approved Reports x 10)
+ (Helpful Likes x 3)
+ (Covered Cities x 2)
+ min(Total Reports, 10)
```

| Signal | Meaning | Weight |
| --- | --- | --- |
| **Approved Reports** | Reports accepted by the Safe Flow admin team | `10 points each` |
| **Helpful Likes** | Likes received from travelers on approved public reports | `3 points each` |
| **Covered Cities** | Distinct cities where the traveler reported events | `2 points each` |
| **Report Activity** | Total submitted reports, capped to avoid spam-driven ranking | `up to 10 points` |

Example:

| Reporter activity | Score impact |
| --- | ---: |
| 5 approved reports | `5 x 10 = 50` |
| 8 helpful likes | `8 x 3 = 24` |
| 2 covered cities | `2 x 2 = 4` |
| 7 total reports | `min(7, 10) = 7` |
| **Community Score** | **85** |

Ranking tie-breakers are applied in this order: highest community score, highest number of approvals, highest number of helpful likes, then user identifier for deterministic ordering.

Top contributors are highlighted across the public report feed with compact badges such as:

<p align="center">
  <img alt="#1 Active Reporter" src="https://img.shields.io/badge/%231%20--%20Active%20Reporter-Top%20Contributor-1EE7A5?style=for-the-badge">
</p>

The badge combines two different signals:

| Badge part | What it means |
| --- | --- |
| **`#1`** | The traveler is first in the community ranking because they currently have the highest community score. |
| **`Active Reporter`** | The traveler trust level, based on their reporting history. |

Trust levels are assigned with these thresholds:

| Trust level | Requirement |
| --- | --- |
| **Trusted Reporter** | At least `20` approved reports and at least `50` helpful likes |
| **Safety Contributor** | At least `10` approved reports or at least `25` helpful likes |
| **Active Reporter** | At least `3` total reports |
| **New Reporter** | Default level for new or low-activity travelers |

This means a badge like **`#1 - Active Reporter`** identifies a traveler who is currently first by community score, while their activity level is still classified as Active Reporter. This makes reliable travelers immediately recognizable while keeping every report tied to a clear public identity.

## Stack

Java 17, Maven, JSP, Servlets, Tomcat-compatible WAR packaging, MariaDB/MySQL, SLF4J, Logback.

## Architecture

Safe Flow follows a layered MVC structure:

- `controller/grafico` handles servlet routing and web flow.
- `controller/applicativo` contains application orchestration.
- `dao` manages database persistence and stored procedures.
- `model`, `bean`, and `record` carry domain and view data.
- `WEB-INF/views` contains protected JSP screens.

## Clone And Run

Safe Flow is designed to be opened, studied, modified, and rebuilt from a standard IntelliJ IDEA workflow. The repository contains the project sources, the Maven descriptor, the database scripts, the JSP views, the static assets, and the MVC layers required to continue development.

Before starting, install:

| Tool | Purpose |
| --- | --- |
| **IntelliJ IDEA** | Main development environment |
| **Java 17** | Project runtime and compilation target |
| **Maven** | Dependency resolution and WAR build lifecycle |
| **MySQL or MariaDB** | Application persistence |
| **Apache Tomcat** | Local servlet container |

Clone the repository and open it from IntelliJ:

```bash
git clone <repository-url>
cd RouteX_MVC_Project
```

In IntelliJ, use:

```text
File -> Open -> RouteX_MVC_Project
```

IntelliJ reads `pom.xml` and imports the project as a Maven WAR web application. Maven automatically downloads the declared Java dependencies and recreates the generated build structure under `target/` whenever the project is packaged.

Set the project SDK to Java 17:

```text
File -> Project Structure -> Project SDK -> Java 17
```

Reload Maven from the Maven tool window:

```text
Maven -> Reload All Maven Projects
```

Initialize the database from MySQL:

```sql
SOURCE Database/SafeFlow_Update.sql;
SOURCE Database/SafeFlow_users_grants.sql;
```

Check the database connection settings in:

```text
src/main/resources/db.properties
```

Build the application:

```bash
mvn package
```

This command compiles the Java code, copies resources and web assets, and generates:

```text
target/SafeFlow_MVC_Project.war
```

Maven recreates only the generated build output, especially `target/`. It does not recreate source files, JSP pages, SQL scripts, images, or database data; those must remain versioned in Git.

To run the application from IntelliJ, create a local Tomcat configuration:

```text
Run -> Edit Configurations -> + -> Tomcat Server -> Local
```

Then add the web artifact:

```text
Deployment -> + -> Artifact -> SafeFlow_MVC_Project:war exploded
```

Use this application context:

```text
/RouteX_MVC_Project
```

The local application will then be available at:

```text
http://localhost:8080/RouteX_MVC_Project/
```

For daily development, edit the Java controllers, models, DAO classes, JSP views, CSS, and assets directly inside IntelliJ, then rebuild or restart Tomcat when needed. Do not run `mvn deploy` unless a remote Maven repository is configured. For this project, `mvn package` is the correct build command.

## Run

```bash
mvn package
```

Deploy the generated WAR on Tomcat after loading the SQL files in `Database/`.

## Ownership

Product vision and ownership: **Simone Remoli**.
