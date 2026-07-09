@echo off
echo Compiling Java files...
if not exist out mkdir out
javac -d out -cp mysql-connector-j-9.1.0.jar db\DBConnection.java model\Employee.java dao\EmployeeDAO.java ui\EmployeeManagementSystem.java
if %errorlevel% equ 0 (
    echo Starting Employee Management System...
    java -cp out;mysql-connector-j-9.1.0.jar ui.EmployeeManagementSystem
) else (
    echo Compilation failed.
)
pause
