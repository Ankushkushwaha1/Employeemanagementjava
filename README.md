# Employee Management System

A desktop-based Employee Management System built using **Java Swing** for the graphical user interface (GUI) and **MySQL** for data persistence.

---

## 🚀 How to Run the Project

Follow these steps to set up and run the project on your local machine:

### 📋 Prerequisites

Before running the application, make sure you have:
1. **Java Development Kit (JDK 8 or higher)** installed.
2. **MySQL Server** installed and running.

---

### ⚙️ Database Configuration

By default, the application is configured to connect to your local MySQL server with the following credentials:
* **Host**: `127.0.0.1:3306` (localhost)
* **Username**: `root`
* **Password**: `Ankit@789`

#### Updating Database Credentials:
If your MySQL root password is different, update it before running:
1. Open the file `db/DBConnection.java`.
2. Locate the line with `PASSWORD`:
   ```java
   private static final String PASSWORD = "YOUR_PASSWORD_HERE";
   ```
3. Replace `"Ankit@789"` with your actual MySQL root password.

> [!NOTE]
> You do **not** need to manually create the database or tables. The application will automatically create the `employee_management` database and the `employees` table on startup if they do not exist.

---

### 💻 Running the Application

Choose the method below based on your operating system or preferred environment:

#### Option 1: On Windows (Batch Script)
Double-click `run.bat` in the project root directory, or run it via Command Prompt:
```cmd
run.bat
```

#### Option 2: On macOS / Linux (Shell Script)
Open your terminal in the project directory, grant execution permissions, and run the script:
```bash
chmod +x run.sh
./run.sh
```

#### Option 3: Using an IDE (IntelliJ IDEA / Eclipse)
1. Open your IDE and select **Open Project**.
2. Navigate to and select this project directory.
3. The IDE will automatically configure the project using the included configuration files.
4. Locate `ui/EmployeeManagementSystem.java`, right-click, and select **Run**.

---

## 📂 Project Structure

```
├── dao/                         # Data Access Objects (handles DB queries)
│   └── EmployeeDAO.java
├── db/                          # Database connection and setup helper
│   └── DBConnection.java
├── model/                       # Data models (Employee object structure)
│   └── Employee.java
├── ui/                          # GUI components (Swing frames/panels)
│   └── EmployeeManagementSystem.java
├── mysql-connector-j-9.1.0.jar  # JDBC driver for MySQL connection
├── run.sh                       # Start script for macOS/Linux
├── run.bat                      # Start script for Windows
└── README.md                    # Project documentation (this file)
```
