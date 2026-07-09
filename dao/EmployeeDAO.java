package dao;

import db.DBConnection;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO - Data Access Object for the employees table.
 * All queries use PreparedStatement to prevent SQL injection.
 */
public class EmployeeDAO {

    /** Insert a new employee. Returns true on success. */
    public boolean addEmployee(Employee e) throws SQLException {
        String sql = "INSERT INTO employees (name, department, designation, salary, phone, email, address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getDepartment());
            ps.setString(3, e.getDesignation());
            ps.setDouble(4, e.getSalary());
            ps.setString(5, e.getPhone());
            ps.setString(6, e.getEmail());
            ps.setString(7, e.getAddress());
            return ps.executeUpdate() > 0;
        }
    }

    /** Update an existing employee by ID. */
    public boolean updateEmployee(Employee e) throws SQLException {
        String sql = "UPDATE employees SET name=?, department=?, designation=?, salary=?, " +
                     "phone=?, email=?, address=? WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getDepartment());
            ps.setString(3, e.getDesignation());
            ps.setDouble(4, e.getSalary());
            ps.setString(5, e.getPhone());
            ps.setString(6, e.getEmail());
            ps.setString(7, e.getAddress());
            ps.setInt(8, e.getEmployeeId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Delete employee by ID. */
    public boolean deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Find employee by ID. Returns null when not found. */
    public Employee getEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /** Retrieve all employees. */
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Map a ResultSet row to an Employee object. */
    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("designation"),
                rs.getDouble("salary"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address")
        );
    }
}
