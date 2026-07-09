package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * EmployeeManagementSystem - Main Swing application window.
 * Provides Add / Update / Delete / Search / Clear / View All operations
 * on the employees table using EmployeeDAO.
 */
public class EmployeeManagementSystem extends JFrame {

    // ---- Form fields ----
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtDepartment = new JTextField();
    private final JTextField txtDesignation = new JTextField();
    private final JTextField txtSalary = new JTextField();
    private final JTextField txtPhone = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextArea  txtAddress = new JTextArea(3, 20);

    // ---- Table ----
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Department", "Designation", "Salary", "Phone", "Email", "Address"}, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final EmployeeDAO dao = new EmployeeDAO();

    // Simple regexes for validation
    private static final Pattern EMAIL_RE = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
    private static final Pattern PHONE_RE = Pattern.compile("^\\d{7,15}$");

    public EmployeeManagementSystem() {
        super("Employee Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadAllEmployees();
    }

    // ---------------- UI builders ----------------

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Employee Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Employee ID:", "Name:", "Department:", "Designation:",
                           "Salary:", "Phone:", "Email:", "Address:"};
        JComponent[] fields = {txtId, txtName, txtDepartment, txtDesignation,
                               txtSalary, txtPhone, txtEmail, new JScrollPane(txtAddress)};

        txtId.setToolTipText("Required for Update / Delete / Search. Leave blank when Adding.");

        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            form.add(new JLabel(labels[i]), g);
            g.gridx = 1; g.weightx = 1;
            fields[i].setPreferredSize(new Dimension(220, 28));
            form.add(fields[i], g);
        }
        return form;
    }

    private JScrollPane buildTablePanel() {
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new TitledBorder("Employees"));
        return sp;
    }

    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton search = new JButton("Search");
        JButton clear = new JButton("Clear");
        JButton viewAll = new JButton("View All");

        add.addActionListener(e -> onAdd());
        update.addActionListener(e -> onUpdate());
        delete.addActionListener(e -> onDelete());
        search.addActionListener(e -> onSearch());
        clear.addActionListener(e -> clearForm());
        viewAll.addActionListener(e -> loadAllEmployees());

        // Selecting a row populates the form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) populateFormFromRow(row);
        });

        p.add(add); p.add(update); p.add(delete);
        p.add(search); p.add(clear); p.add(viewAll);
        return p;
    }

    // ---------------- Actions ----------------

    private void onAdd() {
        Employee e = readForm(false);
        if (e == null) return;
        try {
            if (dao.addEmployee(e)) {
                info("Employee added successfully.");
                clearForm();
                loadAllEmployees();
            } else {
                error("Failed to add employee.");
            }
        } catch (Exception ex) { error("Error: " + ex.getMessage()); }
    }

    private void onUpdate() {
        Employee e = readForm(true);
        if (e == null) return;
        try {
            if (dao.updateEmployee(e)) {
                info("Employee updated successfully.");
                loadAllEmployees();
            } else {
                error("No employee found with ID " + e.getEmployeeId());
            }
        } catch (Exception ex) { error("Error: " + ex.getMessage()); }
    }

    private void onDelete() {
        Integer id = parseId();
        if (id == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete employee with ID " + id + " ?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (dao.deleteEmployee(id)) {
                info("Employee deleted.");
                clearForm();
                loadAllEmployees();
            } else {
                error("No employee found with ID " + id);
            }
        } catch (Exception ex) { error("Error: " + ex.getMessage()); }
    }

    private void onSearch() {
        Integer id = parseId();
        if (id == null) return;
        try {
            Employee e = dao.getEmployeeById(id);
            if (e == null) { error("No employee found with ID " + id); return; }
            fillForm(e);
            tableModel.setRowCount(0);
            addRow(e);
        } catch (Exception ex) { error("Error: " + ex.getMessage()); }
    }

    private void loadAllEmployees() {
        try {
            List<Employee> list = dao.getAllEmployees();
            tableModel.setRowCount(0);
            for (Employee e : list) addRow(e);
        } catch (Exception ex) {
            System.err.println("Failed to load employees from database: " + ex.getMessage());
            error("Failed to load employees: " + ex.getMessage());
        }
    }

    // ---------------- Helpers ----------------

    private void addRow(Employee e) {
        tableModel.addRow(new Object[]{
                e.getEmployeeId(), e.getName(), e.getDepartment(), e.getDesignation(),
                e.getSalary(), e.getPhone(), e.getEmail(), e.getAddress()
        });
    }

    private void populateFormFromRow(int row) {
        txtId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtDepartment.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtDesignation.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtSalary.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        txtPhone.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        txtEmail.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        txtAddress.setText(String.valueOf(tableModel.getValueAt(row, 7)));
    }

    private void fillForm(Employee e) {
        txtId.setText(String.valueOf(e.getEmployeeId()));
        txtName.setText(e.getName());
        txtDepartment.setText(e.getDepartment());
        txtDesignation.setText(e.getDesignation());
        txtSalary.setText(String.valueOf(e.getSalary()));
        txtPhone.setText(e.getPhone());
        txtEmail.setText(e.getEmail());
        txtAddress.setText(e.getAddress());
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtDepartment.setText("");
        txtDesignation.setText(""); txtSalary.setText(""); txtPhone.setText("");
        txtEmail.setText(""); txtAddress.setText("");
        table.clearSelection();
    }

    private Integer parseId() {
        String s = txtId.getText().trim();
        if (s.isEmpty()) { error("Please enter Employee ID."); return null; }
        try { return Integer.parseInt(s); }
        catch (NumberFormatException ex) { error("Employee ID must be a number."); return null; }
    }

    /**
     * Read & validate form values. If requireId is true, employee_id must be present.
     */
    private Employee readForm(boolean requireId) {
        String name = txtName.getText().trim();
        String dept = txtDepartment.getText().trim();
        String desg = txtDesignation.getText().trim();
        String salaryStr = txtSalary.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || dept.isEmpty() || desg.isEmpty()
                || salaryStr.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            error("All fields are required.");
            return null;
        }
        double salary;
        try { salary = Double.parseDouble(salaryStr); }
        catch (NumberFormatException e) { error("Salary must be a number."); return null; }
        if (salary < 0) { error("Salary cannot be negative."); return null; }

        if (!PHONE_RE.matcher(phone).matches()) { error("Phone must be 7-15 digits."); return null; }
        if (!EMAIL_RE.matcher(email).matches()) { error("Invalid email format."); return null; }

        Employee e = new Employee(0, name, dept, desg, salary, phone, email, address);
        if (requireId) {
            Integer id = parseId();
            if (id == null) return null;
            e.setEmployeeId(id);
        }
        return e;
    }

    private void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error",   JOptionPane.ERROR_MESSAGE); }

    // ---------------- main ----------------
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new EmployeeManagementSystem().setVisible(true));
    }
}
