# Final Project - Java Web App

A Spring Boot multi-module project consisting of three related applications in one repository:

- **api** — a REST API that acts as the interface to the database.  
- **core** — the core functionality.
- **client** — a client-side app for executing tasks on users machine.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Features](#features)
3. [Technologies](#technologies)
4. [Public APIs](#public-apis)
5. [Prerequisites](#prerequisites)
6. [Configuration](#configuration)
7. [Notes](#notes)

---

## Project Structure

```
JavaWebAppFinalProject/
├── api/
│ ├── src/
│ └── pom.xml
├── core/
│ ├── src/
│ └── pom.xml
└── client/
  ├── source/
  ├── bin/
  └── compile scripts/
```

Each module has its own `pom.xml` and can be run independently.
`client` is not a module but a directory containing the client-side app for remote-prompted local execution.

---

## Features

- REST API exposing endpoints for application data.
  - Produces and uses JWTs for authenticated access to the database.
- Core services that:
  - Host web pages as user interfaces.
    - Login/Signup
    - Student overview
      - Assignment turn-in
      - Download generated grade reports in PDF format along with their detached digital signature as a .zip archive
    - Professor overview
      - Assignment creation and grading
        - AI writing detection
    - Administrative actions
      - Professor authorization
      - User account manipulation
      - New course input
    - Student grading
  - Use Cron scheduler for periodic job execution.
  - Generate PDFs with detached digital signatures using a PKCS#12 keystore.
  - Send e-mails with attachments to users
- Client-side app
  - If ran by an implemented custom protocol prompts user to input a bandwidth limit and downloads files from a URL supplied by the protocol.
  - If ran by the user prompts them to select a PDF file and it's detached signature to verify validity.

---

## Technologies

- Maven
- Spring Boot
- MySQL
- H2
- MyBatis
- JSON Web Token
- Thymeleaf
- Flyway
- iText
- jsoup
- jUnit
- Lombok
- Tkinter
- curl
- OpenSSL

---

## Public APIs

- API Ninjas - World Time
  - [https://api.api-ninjas.com/v1/worldtime](https://api.api-ninjas.com/v1/worldtime)
- Sapling AI - AI Detect
  - [https://api.sapling.ai/api/v1/aidetect](https://api.sapling.ai/api/v1/aidetect)

---

## Prerequisites

- Java 17+
- Maven 3.6+
- Python 3.0+
- Windows 8+

---

## Configuration

Typical configuration points:

- `application.properties` for each app (ports, database connection, etc.).
- Different apps use different server ports:

    #### api/src/main/resources/application.properties
    `server.port=8081`

    #### core/src/main/resources/application.properties
    `server.port=8080`

- A PKCS#12 keystore under `core/src/main/resources/other`.
  - A corresponding `cert.pem` file under `client/bin` and `core/src/main/resources/other`.

---

## Notes

### Client-side `jwafp://` protocol handler

- **Handler & install path:** `jwafp_handler.exe` is installed, by default, to `C:\Program Files (x86)\JWAFP\jwafp_handler.exe` alongside `cert.pem`.  
- **Protocol format:**  
  `jwafp://download?url=<URL>` — `<URL>` must be percent-encoded.
- **Registry / launch:** Installer must register the protocol so the command is exactly:  
  `"<path>\protocol_handler.exe" "%1"`  
  Wrong quoting or registering under the wrong hive will break launches.
- **Native DLL** The EXE uses a native DLL requiring minimum version of Windows: Windows 8 (WINVER, _WIN32_WINNT = 0x0602).
- **Quick local test:**
  `"C:\Program Files (x86)\JWAFP\jwafp_handler.exe" "jwafp://download?url=..."`

---

© 2025 Tim Pavić
