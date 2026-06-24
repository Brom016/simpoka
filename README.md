# SIMPOKA - Activity Monitor Application

SIMPOKA (Sistem Informasi Monitoring Program Kerja Organisasi atau UKM) is a comprehensive Java Swing desktop application for monitoring and managing activities across organizations.

## Project Overview

SIMPOKA is an Activity Monitor system designed to:
- Track and manage organizational activities
- Monitor activity status (Planned, Ongoing, Completed)
- Generate reports in PDF format
- Support multiple users and organizations
- Provide role-based access control (Admin, Staff)
- Validate and export activity data

## Latest Features (Version 1.0)

### Quick Status Change Feature
- Ubah Status option in activity menu (3-dot menu)
- Fast dropdown-based status updates without full form editing
- Instant database updates with immediate feedback
- Admin-only access with permission validation
- Dialog interface for status selection

### Core Features
- User authentication (Login/Logout)
- Activity management (Create, Read, Update, Delete)
- Search and filter activities by keyword
- PDF export reports for activities
- Organization and user management
- Role-based access control

## Folder Structure

```
simpoka/
├─ src/                              Java source files
│  └─ com/activitymonitor/
│     ├─ App.java                    Main entry point
│     ├─ controller/
│     │  ├─ AuthController.java      Authentication logic
│     │  └─ ActivityController.java  Activity business logic
│     ├─ dao/                        Data Access Objects
│     │  ├─ ActivityDAO.java         Activity database operations
│     │  ├─ UserDAO.java             User database operations
│     │  └─ OrganizationDAO.java     Organization database operations
│     ├─ model/                      Entity models
│     │  ├─ Activity.java
│     │  ├─ User.java
│     │  └─ Organization.java
│     ├─ util/                       Utility classes
│     │  ├─ DBConnection.java        Database connection management
│     │  ├─ InputValidator.java      Form input validation
│     │  ├─ PDFExporter.java         PDF generation and export
│     │  ├─ SessionManager.java      User session management
│     │  └─ UIConstants.java         UI styling constants
│     └─ view/                       UI Components
│        ├─ DashboardFrame.java      Main application window
│        ├─ LoginFrame.java          Login screen
│        ├─ ActivityTablePanel.java  Activity list with menu
│        ├─ ActivityFormPanel.java   Activity add/edit form
│        └─ ExportPDFDialog.java     PDF export dialog
├─ bin/                              Compiled .class files
├─ lib/                              External dependencies
│  ├─ mysql-connector-j-9.7.0.jar
│  └─ pdfa-7.1.3.jar
├─ db/                               Database schema
│  └─ schema.sql
├─ output/                           PDF export output folder
├─ Diagram/                          Project diagrams
│  ├─ ERD-SIMPOKA.png
│  └─ FLOWCHART-SIMPOKA.png
├─ TIMELINE.md                       Project timeline and milestones
└─ README.md                         This file
```

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 11+ |
| GUI Framework | Java Swing (JFrame, JPanel, JTable) |
| Database | MySQL 5.7+ / MariaDB 10.3+ |
| JDBC Driver | mysql-connector-j 9.7.0 |
| PDF Library | pdfa 7.1.3 |
| Build Tool | javac (Java Compiler) |
| IDE Supported | VS Code with Java Extension Pack |

## System Requirements

- Java 11 or higher
- MySQL 5.7+ or MariaDB 10.3+
- Minimum 200MB disk space
- Minimum 1GB RAM
- Windows, Linux, or macOS with Swing support

## Installation and Setup

### 1. Clone/Download Project

```bash
git clone https://github.com/Brom016/simpoka.git
cd simpoka
```

### 2. Database Setup

Execute the schema file in MySQL:

```bash
mysql -u root -p < db/schema.sql
```

Or manually:

```bash
mysql -u root -p
CREATE DATABASE simpoka;
USE simpoka;
source db/schema.sql;
```

### 3. Configure Database Connection

Edit `src/com/activitymonitor/util/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/simpoka";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 4. Compile the Application

From the project root directory:

```bash
cd d:\simpoka
javac -d bin -cp "bin;lib/*" -sourcepath src src/com/activitymonitor/App.java
```

### 5. Run the Application

```bash
java -cp "bin;lib/*" com.activitymonitor.App
```

Alternatively, use VS Code with the debug configuration: `Launch App (com.activitymonitor)`

## Project Structure Description

### Controllers

- **AuthController.java**: Manages user authentication and login flow
- **ActivityController.java**: Handles activity CRUD operations and business logic

### Data Models

- **Activity.java**: Represents an activity entity with properties (name, date, location, status, etc.)
- **User.java**: Represents a user with role information (admin or staff)
- **Organization.java**: Represents an organization that groups users and activities

### Data Access Objects (DAO)

- **ActivityDAO.java**: Database operations for activities (find, insert, update, delete, updateStatus)
- **UserDAO.java**: Database operations for users
- **OrganizationDAO.java**: Database operations for organizations

### Utilities

- **DBConnection.java**: Manages MySQL database connections using JDBC
- **InputValidator.java**: Validates form inputs for activities and users
- **PDFExporter.java**: Generates PDF reports from activity data
- **SessionManager.java**: Manages user sessions during application runtime
- **UIConstants.java**: Contains UI styling constants and reusable components

### Views (User Interface)

- **DashboardFrame.java**: Main application window with tabs for activities and reports
- **LoginFrame.java**: Login dialog for user authentication
- **ActivityTablePanel.java**: Displays activities in a table with search, add, edit, delete, and status change options
- **ActivityFormPanel.java**: Dialog for adding and editing activities
- **ExportPDFDialog.java**: Dialog for exporting activities to PDF format

## Usage Examples

### User Authentication

```java
AuthController auth = new AuthController(loginFrame);
User user = auth.authenticate("admin", "password");
if (user != null) {
    new DashboardFrame(user).setVisible(true);
}
```

### Create New Activity

```java
Activity activity = new Activity(
    0, 
    "Seminar Teknologi", 
    "Tech seminar discussion", 
    Date.valueOf("2026-06-15"), 
    "Gedung A", 
    50, 
    "planned", 
    userId, 
    orgId
);
activityDAO.insert(activity);
```

### Change Activity Status

```java
int activityId = 5;
String newStatus = "ongoing";
boolean success = activityDAO.updateStatus(activityId, newStatus);
if (success) {
    JOptionPane.showMessageDialog(frame, "Status kegiatan berhasil diperbarui.");
}
```

### Search Activities

```java
List<Activity> results = activityDAO.findByKeyword("seminar", organizationId);
```

### Export Activities to PDF

```java
List<Activity> activities = activityDAO.findAll(organizationId);
Organization org = orgDAO.findById(organizationId);
boolean success = PDFExporter.export(activities, org, "laporan-kegiatan.pdf");
```

## Database Schema

```
organizations
├─ id (Primary Key)
└─ name

users
├─ id (Primary Key)
├─ username
├─ password
├─ role (admin/staff)
└─ organization_id (Foreign Key)

activities
├─ id (Primary Key)
├─ name
├─ description
├─ date
├─ location
├─ participant_count
├─ status (planned, ongoing, completed)
├─ created_by (Foreign Key to users)
└─ organization_id (Foreign Key to organizations)
```

## Testing Checklist

- [ ] Application launches without errors
- [ ] User can log in with valid credentials
- [ ] Activity list displays all activities
- [ ] User can create a new activity
- [ ] User can edit existing activities
- [ ] User can change activity status via 3-dot menu
- [ ] User can delete activities
- [ ] Search functionality filters activities correctly
- [ ] PDF export generates valid documents
- [ ] Logout function works correctly
- [ ] Permission system restricts non-admin access

## Security Considerations

- Password hashing is recommended (currently stores passwords in plain text)
- SQL injection prevention is implemented via PreparedStatement
- Input validation is implemented for all form fields
- Role-based access control is enforced
- Session timeout is recommended for production use

## Support and Contribution

To report issues or contribute to the project:

1. Check existing GitHub issues to avoid duplicates
2. Create detailed bug reports including:
   - Java version
   - MySQL version
   - Complete error message and stack trace
   - Steps to reproduce the issue
   - Expected versus actual behavior
3. Submit pull requests with clear descriptions of changes
4. Follow existing code style and conventions

## License

This project is developed for activity monitoring purposes. See LICENSE file for details.

## Project Information

- Repository: https://github.com/Brom016/simpoka
- Main Developer: Brom016
- Last Updated: June 8, 2026
- Current Version: 1.0 with Status Change Feature
- Minimum Java Version: Java 11
- Status: Active Development

---

For additional project information, see TIMELINE.md for milestones and project history.