# Smart Campus - Sensor & Room Management API

**Module:** Client-Server Architectures (5COSC022W)  
**Academic Year:** 2025/26

---

## 1. API Design Overview

The **Smart Campus API** is a robust, highly available RESTful web service designed to manage campus infrastructure.  
Built using **JAX-RS (Jakarta RESTful Web Services)**, the system provides a seamless interface for facilities managers to track and manage thousands of physical **Rooms**, the hardware **Sensors** deployed within them (e.g., CO2 monitors, occupancy trackers), and historical **SensorReadings**.

The architecture strictly adheres to REST principles, utilizing:

- hierarchical resource routing
- semantic HTTP status codes
- centralized exception mapping for resilient error handling
- in-memory thread-safe data structures to guarantee system integrity without reliance on external database technologies

---

## 2. Build & Launch Instructions

### Prerequisites

- **Java Development Kit (JDK):** Version 11 or higher
- **Apache Maven:** Installed and configured in the system `PATH`
- **Apache Tomcat Server:** Version 9.x (see justification and setup below)

---

### Server Selection: Why Tomcat 9?

For this specific coursework, **Apache Tomcat 9** is the strictly recommended server environment.

#### Technical Justification

The application utilizes the `javax.ws.rs.*` namespace for JAX-RS annotations, which aligns with **Java EE 8** specifications.

Apache Tomcat 10 and newer versions have fully migrated to the new `jakarta.*` namespace. Deploying this application on Tomcat 10+ without a bytecode conversion tool will result in `ClassNotFoundException` and immediate deployment failure.

Tomcat 9 provides native, out-of-the-box compatibility with the `javax.*` packages required by the coursework rubric.

---

### Step 1: Download & Install Tomcat 9

1. Navigate to the official Apache Tomcat 9 download page:  
   [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)

2. Under the **Core** section, download the appropriate distribution:
   - **Windows:** Download the `64-bit Windows zip` or the `32-bit/64-bit Windows Service Installer`  
     (the `.zip` is recommended for development as it does not require administrator installation).
   - **macOS / Linux:** Download the `tar.gz` archive.

3. Extract the downloaded archive to a dedicated directory on your local machine, for example:
   - `C:\Tomcat9` (Windows)
   - `~/dev/tomcat9` (macOS/Linux)

---

### Step 2: Build the Application

1. Clone the repository and navigate to the project root:

```bash
git clone https://github.com/Ahinthaj/SmartCampusAPI.git
cd SmartCampusAPI
```

2. Compile and package the application into a deployable `.war` file using Maven:

```bash
mvn clean install
```

3. Upon successful build, Maven will generate a `.war` file (e.g., `SmartCampusAPI-1.0.war`) in the `/target` directory.

---

### Step 3: Deploy to Tomcat

1. Locate the generated `.war` file in the project's `/target` folder.
2. Copy the `.war` file.
3. Navigate to your extracted Tomcat 9 directory and open the `webapps` folder.
4. Paste the `.war` file directly into the `webapps` folder.
5. *(Optional)* Rename the file to `api.war` to shorten the base URL when making requests.

---

### Step 4: Launch the Server

Navigate to the `/bin` directory inside your Tomcat folder and start the server from a terminal/command prompt.

#### Windows

```bat
cd C:\Tomcat9\bin
startup.bat
```

#### macOS / Linux

```bash
cd ~/dev/tomcat9/bin
chmod +x *.sh
./startup.sh
```

Tomcat will automatically unpack the `.war` file and deploy the RESTful service.

---

### Step 5: Verify Deployment

To confirm the server is running and the API is correctly mapped, open a web browser or API client (e.g., Postman) and navigate to the discovery endpoint:

```text
http://localhost:8080/SmartCampusAPI-1.0/api/v1
```

> If you renamed the `.war` file to `api.war` in Step 3, the URL will be:

```text
http://localhost:8080/api/api/v1
```

A successful deployment returns **HTTP 200 OK** with JSON metadata links.

---

## Notes

- This setup is designed for local development and coursework demonstration.
- Ensure no other service is already using port `8080`.
- If deployment fails, review Tomcat logs in `<tomcat-directory>/logs`.
