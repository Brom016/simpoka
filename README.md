## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Run

- Compile:

```
javac -d bin -sourcepath src src/com/activitymonitor/App.java
```

- Run:

```
java -cp bin com.activitymonitor.App
```

Or use the VS Code debug configuration `Launch App (com.activitymonitor)`.

```
simpoka
├─ Diagram
│  ├─ ERD-SIMPOKA.png
│  └─ FLOWCHART-SIMPOKA.png
├─ README.md
├─ TIMELINE.md
├─ bin
│  └─ com
│     ├─ activitymonitor
│     │  ├─ App.class
│     │  ├─ controller
│     │  │  ├─ ActivityController.class
│     │  │  └─ AuthController.class
│     │  ├─ dao
│     │  │  ├─ Activity.class
│     │  │  ├─ OrganizationDAO.class
│     │  │  └─ UserDAO.class
│     │  ├─ model
│     │  │  ├─ Activity.class
│     │  │  ├─ Organization.class
│     │  │  └─ User.class
│     │  ├─ util
│     │  │  ├─ DBConnection.class
│     │  │  ├─ InputValidator.class
│     │  │  ├─ PDFExporter.class
│     │  │  └─ SessionManager.class
│     │  └─ view
│     └─ folder.md
├─ database
├─ db
│  └─ schema.sql
├─ lib
│  ├─ mysql-connector-j-9.7.0.jar
│  └─ pdfa-7.1.3.jar
├─ output
└─ src
   └─ com
      ├─ activitymonitor
      │  ├─ App.java
      │  ├─ controller
      │  │  ├─ ActivityController.java
      │  │  └─ AuthController.java
      │  ├─ dao
      │  │  ├─ Activity.java
      │  │  ├─ OrganizationDAO.java
      │  │  └─ UserDAO.java
      │  ├─ model
      │  │  ├─ Activity.java
      │  │  ├─ Organization.java
      │  │  └─ User.java
      │  ├─ util
      │  │  ├─ DBConnection.java
      │  │  ├─ InputValidator.java
      │  │  ├─ PDFExporter.java
      │  │  └─ SessionManager.java
      │  └─ view
      └─ folder.md

```