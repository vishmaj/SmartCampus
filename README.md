# Smart Campus Sensor & Room Management API

## Overview
This project was built as part of the coursework submission requirement for the **Client-Server Architectures Module (5COSC022W)**, offered by the **University of Westminster**.

It provides a RESTful API for managing a university **Smart Campus** environment, including:

- Rooms
- Sensors
- Sensor readings (nested sub-resources)

---

## Video Demonstration
[INSERT URL HERE]

---

## Tech Stack

- Java 17+
- JAX-RS (Jersey)
- Maven
- Apache Tomcat 9.x

---

## Server Selection: Why Tomcat 9?

For this coursework, **Apache Tomcat 9** is the recommended server environment.

### Technical Justification
The application uses the `javax.ws.rs.*` namespace for JAX-RS annotations, aligned with **Java EE 8** specifications.

Tomcat 10+ migrated from `javax.*` to `jakarta.*`. Running this project on Tomcat 10+ without migration tooling can cause `ClassNotFoundException` and deployment failure.

Tomcat 9 provides direct compatibility with `javax.*`, which matches the coursework rubric and avoids namespace issues.

---

## Quickstart

### 1) Clone the repository

```bash
git clone https://github.com/Ahinthaj/SmartCampusAPI.git
cd SmartCampusAPI
```

### 2) Build the project with Maven

```bash
mvn clean package
```

### 3) Download and extract Apache Tomcat 9

Download from:  
[https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)

Extract the archive (example path on Windows: `C:\tomcat`).

### 4) Deploy the WAR file to Tomcat

#### Windows (Command Prompt)

```bat
copy target\SmartCampusAPI-1.0.war C:\tomcat\webapps\SmartCampusAPI.war
```

#### macOS / Linux

```bash
cp target/SmartCampusAPI-1.0.war ~/tomcat/webapps/SmartCampusAPI.war
```

### 5) Start Tomcat (from Tomcat `bin` directory)

#### Windows

```bat
cd C:\tomcat\bin
startup.bat
```

#### macOS / Linux

```bash
cd ~/tomcat/bin
chmod +x *.sh
./startup.sh
```

### 6) Access the API

```text
http://localhost:8080/SmartCampusAPI/api/v1/
```

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/` | Discovery & API metadata |
| GET | `/api/v1/rooms` | List all rooms |
| POST | `/api/v1/rooms` | Create a new room |
| GET | `/api/v1/rooms/{roomId}` | Get room details |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room (only if empty) |
| GET | `/api/v1/sensors` | List all sensors |
| GET | `/api/v1/sensors?type={type}` | Filter sensors by type |
| POST | `/api/v1/sensors` | Register a new sensor |
| GET | `/api/v1/sensors/{sensorId}` | Get a sensor by ID |
| GET | `/api/v1/sensors/room/{roomId}` | Get all sensors assigned to a room |
| GET | `/api/v1/sensors/{sensorId}/readings` | Get readings for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading |

---

## Sample cURL Commands

The following commands demonstrate successful interactions with the API and satisfy the coursework requirement for sample requests.

### 1) View API Discovery Metadata

```bash
curl -i -X GET http://localhost:8080/SmartCampusAPI/api/v1/
```

### 2) Register a New Room

```bash
curl -i -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\": \"LIB-301\", \"name\": \"Library Quiet Study\", \"capacity\": 50}"
```

### 3) Register a New Sensor to the Room

```bash
curl -i -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\": \"TEMP-001\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"roomId\": \"LIB-301\"}"
```

### 4) Retrieve Sensors Filtered by Type

```bash
curl -i -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

### 5) Append a New Sensor Reading (Sub-Resource)

```bash
curl -i -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\": 22.5}"
```

### 6) Attempt to Delete an Occupied Room (Expected 409 Conflict)

```bash
curl -i -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```

---

## Conceptual Report (Q&A)

### Part 1: Server Architecture and Setup

#### Question 1
In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

**Answer:**  
By default, JAX-RS resource classes follow a **request-per-lifecycle** model, which means they are **not singletons** unless explicitly configured otherwise. For each incoming API request, the server creates a new instance of the resource class and destroys it after the response is sent.

Because of this lifecycle, storing mutable data in ordinary instance variables (e.g., `ArrayList` inside a resource class) can lead to data loss, since those variables are tied to short-lived instances. To preserve state across requests, data should be stored in memory structures managed outside the request-scoped resource lifecycle (for example, DAO/service-level singleton components). Since web requests are concurrent, thread-safe collections such as `ConcurrentHashMap` should be used to reduce race conditions.

---

#### Question 2
Why is the provision of “Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

**Answer:**  
Hypermedia through **HATEOAS** (Hypermedia as the Engine of Application State) is considered an advanced REST design practice because it makes APIs **self-descriptive**. Instead of hardcoding endpoint paths, clients follow links provided in server responses to discover valid next actions and related resources.

Compared with static documentation alone, this improves **discoverability**, **resilience**, and **decoupling**. If URI structures change internally, clients can still function as long as relation links remain stable. In addition, state transitions can be constrained by business rules, because the server only exposes relevant actions for the current resource state.

---

### Part 2: Room Management

#### Question 1
When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

**Answer:**  
Returning only room IDs reduces response size and bandwidth usage, which is efficient for large collections. However, this shifts work to the client, which must perform additional calls to fetch each room’s details. That often leads to the **N+1 request problem**, increasing latency and client complexity.

Returning full room objects increases payload size but reduces follow-up calls and usually simplifies client-side logic. The choice depends on use case, collection size, and performance goals.

---

#### Question 2
Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**Answer:**  
Yes, the DELETE operation is idempotent. In REST, an operation is idempotent if repeating it results in the same final server state as performing it once.

- **First DELETE** on an existing room (e.g., `TEST-111`) removes the room and returns `204 No Content`.
- **Second identical DELETE** finds no such room and returns `404 Not Found`.

Although the response codes differ, the server state remains unchanged after the first successful deletion (the room stays deleted), so the operation remains idempotent.

---

### Part 3: Sensor Operations & Linking

#### Question 1
We explicitly use the `@Consumes(MediaType.APPLICATION_JSON)` annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as `text/plain` or `application/xml`. How does JAX-RS handle this mismatch?

**Answer:**  
`@Consumes(MediaType.APPLICATION_JSON)` instructs JAX-RS to accept only JSON payloads for that endpoint. If the client sends another content type (e.g., `text/plain` or `application/xml`), JAX-RS rejects the request before business logic runs.

The framework typically returns **HTTP 415 Unsupported Media Type**, preventing unsafe or invalid deserialization attempts and preserving API contract integrity.

---

#### Question 2
You implemented filtering using `@QueryParam`. Contrast this with an alternative design where type is part of the URL path (e.g., `/api/v1/sensors/type/CO2`). Why is the query parameter approach generally considered superior for filtering and searching collections?

**Answer:**  
Path parameters are best suited to identifying concrete resources or hierarchical structure. Query parameters are better for optional modifiers like filtering, searching, sorting, and pagination.

Using query parameters (e.g., `?type=CO2&status=ACTIVE`) is more scalable and flexible because filters can be composed in many combinations without creating many rigid path patterns. If omitted, the endpoint naturally returns the full collection. This aligns with REST conventions for collection querying.

---

### Part 4: Deep Nesting with Sub-Resources

#### Question 1
Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., `sensors/{id}/readings/{rid}`) in one massive controller class?

**Answer:**  
The Sub-Resource Locator pattern improves modularity by separating nested concerns into dedicated classes. This prevents “God classes,” supports the **Single Responsibility Principle**, and keeps routing logic maintainable as the API grows.

Passing a parent identifier into a sub-resource gives contextual routing cleanly, without manual URI parsing in one large class. It also improves team collaboration by reducing merge conflicts and allowing independent development of related but distinct endpoint groups.

---

### Part 5: Advanced Error Handling, Exception Mapping, and Logging

#### Question 1
Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**Answer:**  
`404 Not Found` indicates that the requested endpoint/resource path itself does not exist. In contrast, if the endpoint exists and JSON syntax is valid but a referenced entity (e.g., `roomId`) is invalid in business terms, the request is syntactically correct but semantically unacceptable.

`422 Unprocessable Entity` more accurately communicates this business-validation failure.

---

#### Question 2
From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**Answer:**  
Exposing internal stack traces is an information disclosure risk. Attackers can infer implementation details such as package structure, class names, framework/library versions (e.g., Jersey, Jackson, Tomcat), filesystem paths, and integration components (e.g., drivers/services).

This intelligence can be used to identify known vulnerabilities, craft targeted attacks, and map system internals. Therefore, stack traces should be hidden from clients and replaced with controlled error responses.

---

#### Question 3
Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting `Logger.info()` statements inside every single resource method?

**Answer:**  
JAX-RS filters centralize cross-cutting behavior (logging, auditing, tracing) in one place, enforcing **Separation of Concerns** and **DRY** principles. This avoids duplicating logging code across endpoint methods and keeps resource classes focused on business logic.

With filters, changes to logging formats or telemetry behavior can be implemented once and applied globally, improving maintainability and scalability.

---

## Notes

- Data is stored in memory; restarting the server resets all records.
- Use Tomcat 9 for `javax.*` compatibility.
- If your context path is different, replace `SmartCampusAPI` in URLs accordingly.
