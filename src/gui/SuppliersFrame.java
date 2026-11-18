
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import dao.PurchaseDAO;

public class SuppliersFrame extends JFrame {

    JTextField idField, nameField, phoneField, emailField;
    JTextField cityField, addressField, searchField;

    JTable table;
    DefaultTableModel model;

    PurchaseDAO dao = new PurchaseDAO();

    public SuppliersFrame() {

        setTitle("Supplier Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("SUPPLIER MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        cityField = new JTextField();
        addressField = new JTextField();

        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("City:"));
        formPanel.add(cityField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(
            new String[]{"ID", "Name", "Phone", "Email", "City", "Address"}, 0);

        table = new JTable(model);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addSupplier());
        updateButton.addActionListener(e -> updateSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        searchButton.addActionListener(e -> searchSupplier());
        refreshButton.addActionListener(e -> loadSuppliers());
        backButton.addActionListener(e -> dispose());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();

                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                phoneField.setText(model.getValueAt(row, 2).toString());
                emailField.setText(model.getValueAt(row, 3).toString());
                cityField.setText(model.getValueAt(row, 4).toString());
                addressField.setText(model.getValueAt(row, 5).toString());
            }
        });

        add(mainPanel);

        loadSuppliers();

        setVisible(true);
    }

    private void loadSuppliers() {

        model.setRowCount(0);

        String sql = "SELECT * FROM suppliers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("address")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addSupplier() {

        boolean result = dao.addSupplier(
            nameField.getText(),
            phoneField.getText(),
            emailField.getText(),
            cityField.getText(),
            addressField.getText()
        );

        if (result) {
            JOptionPane.showMessageDialog(this, "Supplier added successfully!");
            loadSuppliers();
        } else {
            JOptionPane.showMessageDialog(this, "Unable to add supplier!");
        }
    }

    private void updateSupplier() {

        try {
            int id = Integer.parseInt(idField.getText());

            boolean result = dao.updateSupplier(
                id,
                phoneField.getText(),
                emailField.getText(),
                cityField.getText(),
                addressField.getText()
            );

            if (result) {
                JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
                loadSuppliers();
            } else {
                JOptionPane.showMessageDialog(this, "Unable to update supplier!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Supplier ID!");
        }
    }

    private void deleteSupplier() {

        try {
            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete this supplier?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
            	dao.deleteSupplier(id);

            	JOptionPane.showMessageDialog(this, "Supplier deleted!");
            	loadSuppliers();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Supplier ID!");
        }
    }

    private void searchSupplier() {

        try {
            int id = Integer.parseInt(idField.getText());

            model.setRowCount(0);

            String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt("supplier_id"),
                            rs.getString("supplier_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("city"),
                            rs.getString("address")
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Supplier not found!");
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Supplier ID!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
