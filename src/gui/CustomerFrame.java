
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.CustomerDAO;
import model.Customer;
import db.DBConnection;

public class CustomerFrame extends JFrame {

    private JTextField idField, nameField, phoneField, emailField;

    private JTable customerTable;
    private DefaultTableModel tableModel;

    private CustomerDAO customerDAO = new CustomerDAO();

    public CustomerFrame() {

        setTitle("VIDYA STORE - Customer Management");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("CUSTOMER MANAGEMENT", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(35, 90, 160));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        add(heading, BorderLayout.NORTH);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        idField = new JTextField();
        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        inputPanel.add(new JLabel("Customer ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        // Buttons
        JButton addButton = new JButton("Add Customer");
        JButton updateButton = new JButton("Update Customer");
        JButton deleteButton = new JButton("Delete Customer");
        JButton searchButton = new JButton("Search Customer");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 8, 8));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.CENTER);

        // Customer table
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Phone", "Email"}, 0);

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Customer Details"));

        add(scrollPane, BorderLayout.SOUTH);

        // Add Customer
        addButton.addActionListener(e -> {

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty()
                    || !phone.matches("[0-9]{10}")
                    || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                JOptionPane.showMessageDialog(this,
                        "Enter a valid name, 10-digit phone and email!");
                return;
            }

            Customer customer = new Customer(0, name, phone, email);

            if (customerDAO.addCustomer(customer)) {

                JOptionPane.showMessageDialog(this,
                        "Customer added successfully!");

                clearFields();
                loadCustomers();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add customer!");
            }
        });

        // Update Customer
        updateButton.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());

                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();

                if (id <= 0 || name.isEmpty()
                        || !phone.matches("[0-9]{10}")
                        || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                    JOptionPane.showMessageDialog(this,
                            "Enter valid customer details!");
                    return;
                }

                if (customerDAO.updateCustomer(id, name, phone, email)) {

                    JOptionPane.showMessageDialog(this,
                            "Customer updated successfully!");

                    clearFields();
                    loadCustomers();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Customer not found or update failed!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid Customer ID!");
            }
        });

        // Delete Customer
        deleteButton.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());

                if (id <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid Customer ID!");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this customer?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {

                    if (customerDAO.deleteCustomer(id)) {

                        JOptionPane.showMessageDialog(this,
                                "Customer deleted successfully!");

                        clearFields();
                        loadCustomers();

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Customer not found or cannot be deleted!");
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid Customer ID!");
            }
        });

        // Search Customer
        searchButton.addActionListener(e -> {

            try {

                int id = Integer.parseInt(idField.getText());

                String sql = "SELECT * FROM customers WHERE customer_id = ?";

                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {

                            nameField.setText(rs.getString("customer_name"));
                            phoneField.setText(rs.getString("phone"));
                            emailField.setText(rs.getString("email"));

                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Customer not found!");
                        }
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid Customer ID!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Search failed: " + ex.getMessage());
            }
        });

        // Refresh
        refreshButton.addActionListener(e -> loadCustomers());
        backButton.addActionListener(e -> dispose());

        // Select table row
        customerTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && customerTable.getSelectedRow() != -1) {

                int row = customerTable.getSelectedRow();

                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                phoneField.setText(tableModel.getValueAt(row, 2).toString());
                emailField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        loadCustomers();

        setVisible(true);
    }

    // Load customers from MySQL
    private void loadCustomers() {

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM customers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                tableModel.addRow(new Object[]{
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to load customers: " + e.getMessage());
        }
    }

    // Clear fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerFrame());
    }
}
