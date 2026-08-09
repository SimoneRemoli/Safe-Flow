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

## Stack

Java 17, Maven, JSP, Servlets, Tomcat-compatible WAR packaging, MariaDB/MySQL, SLF4J, Logback.

## Architecture

Safe Flow follows a layered MVC structure:

- `controller/grafico` handles servlet routing and web flow.
- `controller/applicativo` contains application orchestration.
- `dao` manages database persistence and stored procedures.
- `model`, `bean`, and `record` carry domain and view data.
- `WEB-INF/views` contains protected JSP screens.

## Run

```bash
mvn package
```

Deploy the generated WAR on Tomcat after loading the SQL files in `Database/`.

## Ownership

Product vision and ownership: **Simone Remoli**.
