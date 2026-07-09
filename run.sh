#!/bin/bash

# Navigate to the script's directory
cd "$(dirname "$0")"

echo "Compiling Java files..."
javac -d out -cp mysql-connector-j-9.1.0.jar db/DBConnection.java model/Employee.java dao/EmployeeDAO.java ui/EmployeeManagementSystem.java

if [ $? -eq 0 ]; then
    echo "Starting Employee Management System..."
    java -cp out:mysql-connector-j-9.1.0.jar ui.EmployeeManagementSystem
else
    echo "Compilation failed."
fi
