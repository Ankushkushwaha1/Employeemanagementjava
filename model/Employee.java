package model;

/**
 * Employee - POJO representing a row in the employees table.
 */
public class Employee {
    private int employeeId;
    private String name;
    private String department;
    private String designation;
    private double salary;
    private String phone;
    private String email;
    private String address;

    public Employee() { }

    public Employee(int employeeId, String name, String department, String designation,
                    double salary, String phone, String email, String address) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // ---- Getters & Setters ----
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
