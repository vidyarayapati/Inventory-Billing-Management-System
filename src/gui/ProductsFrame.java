
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.ProductDAO;
import db.DBConnection;
import model.Product;

public class ProductsFrame extends JFrame {

    private JTextField idField, nameField, categoryField;
    private JTextField priceField, quantityField;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private ProductDAO productDAO = new ProductDAO();

    public ProductsFrame() {

        setTitle("VIDYA STORE - Product Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("PRODUCT MANAGEMENT", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(35, 90, 160));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        add(heading, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        idField = new JTextField();
        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        // Buttons
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton searchButton = new JButton("Search Product");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Product Table
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Category", "Price", "Quantity"}, 0);

        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Products"));

        add(scrollPane, BorderLayout.CENTER);

        // Add Product

addButton.addActionListener(e -> {

    try {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (name.isEmpty() || category.isEmpty()
                || priceText.isEmpty() || quantityText.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill Name, Category, Price and Quantity!");
            return;
        }

        double price = Double.parseDouble(priceText);
        int quantity = Integer.parseInt(quantityText);

        if (price <= 0 || quantity < 0) {
            JOptionPane.showMessageDialog(this,
                    "Price must be positive and quantity cannot be negative!");
            return;
        }

        Product product = new Product(0, name, category, price, quantity);

        if (productDAO.addProduct(product)) {
            JOptionPane.showMessageDialog(this,
                    "Product added successfully!");
            clearFields();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add product!");
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
                "Enter numbers only for Price and Quantity!");
    }
});


        // Update Product
        updateButton.addActionListener(e -> {

            try {
                int id = Integer.parseInt(idField.getText());
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (id <= 0 || price <= 0 || quantity < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Enter valid product details!");
                    return;
                }

                if (productDAO.updateProduct(id, price, quantity)) {

                    JOptionPane.showMessageDialog(this,
                            "Product updated successfully!");

                    clearFields();
                    loadProducts();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Product not found or update failed!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter valid Product ID, price and quantity!");
            }
        });

        // Delete Product
        deleteButton.addActionListener(e -> {

            try {
                int id = Integer.parseInt(idField.getText());

                if (id <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid Product ID!");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this product?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {

                    if (productDAO.deleteProduct(id)) {

                        JOptionPane.showMessageDialog(this,
                                "Product deleted successfully!");

                        clearFields();
                        loadProducts();

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Cannot delete this product because it is already used in previous bills.!");
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid Product ID!");
            }
        });

        // Search Product
        searchButton.addActionListener(e -> {

            try {
                int id = Integer.parseInt(idField.getText());

                Product product = productDAO.getProductById(id);

                if (product != null) {

                    nameField.setText(product.getProductName());
                    categoryField.setText(product.getCategory());
                    priceField.setText(String.valueOf(product.getPrice()));
                    quantityField.setText(String.valueOf(product.getQuantity()));

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Product not found!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid Product ID!");
            }
        });

        // Refresh Table
        refreshButton.addActionListener(e -> loadProducts());
        backButton.addActionListener(e -> dispose());

        // Select a row from table
        productTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && productTable.getSelectedRow() != -1) {

                int row = productTable.getSelectedRow();

                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                categoryField.setText(tableModel.getValueAt(row, 2).toString());
                priceField.setText(tableModel.getValueAt(row, 3).toString());
                quantityField.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        loadProducts();

        setVisible(true);
    }

    // Load products from MySQL into the table
    private void loadProducts() {

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                tableModel.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to load products: " + e.getMessage());
        }
    }

    // Clear input fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductsFrame());
    }
}
