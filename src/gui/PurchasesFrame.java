
package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;
import dao.PurchaseDAO;

public class PurchasesFrame extends JFrame {

    JComboBox<SupplierOption> supplierBox;
    JComboBox<ProductOption> productBox;

    JTextField quantityField;
    JTextField priceField;

    PurchaseDAO dao = new PurchaseDAO();

    public PurchasesFrame() {

        setTitle("Purchase Management");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("PURCHASE MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 20));

        supplierBox = new JComboBox<>();
        productBox = new JComboBox<>();

        quantityField = new JTextField();
        priceField = new JTextField();

        formPanel.add(new JLabel("Select Supplier:"));
        formPanel.add(supplierBox);

        formPanel.add(new JLabel("Select Product:"));
        formPanel.add(productBox);

        formPanel.add(new JLabel("Purchase Quantity:"));
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Purchase Price:"));
        formPanel.add(priceField);

        JButton purchaseButton = new JButton("Add Purchase");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(purchaseButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        purchaseButton.addActionListener(e -> addPurchase());

        refreshButton.addActionListener(e -> {
            loadSuppliers();
            loadProducts();
        });

        backButton.addActionListener(e -> dispose());

        productBox.addActionListener(e -> {
            ProductOption selected =
                (ProductOption) productBox.getSelectedItem();

            if (selected != null) {
                priceField.setText(String.valueOf(selected.price));
            }
        });

        add(mainPanel);

        loadSuppliers();
        loadProducts();

        setVisible(true);
    }

    private void loadSuppliers() {

        supplierBox.removeAllItems();

        String sql = "SELECT supplier_id, supplier_name FROM suppliers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                supplierBox.addItem(new SupplierOption(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name")
                ));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadProducts() {

        productBox.removeAllItems();

        String sql = "SELECT product_id, product_name, price, quantity FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productBox.addItem(new ProductOption(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addPurchase() {

        try {

            SupplierOption supplier =
                (SupplierOption) supplierBox.getSelectedItem();

            ProductOption product =
                (ProductOption) productBox.getSelectedItem();

            if (supplier == null || product == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a supplier and product!");
                return;
            }

            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            if (quantity <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Quantity and price must be greater than zero!");
                return;
            }

            boolean result = dao.addPurchase(
                supplier.id,
                product.id,
                quantity,
                price
            );

            if (result) {

                JOptionPane.showMessageDialog(this,
                    "Purchase added successfully!\nStock updated!");

                quantityField.setText("");
                loadProducts();

            } else {

                JOptionPane.showMessageDialog(this,
                    "Unable to add purchase!");

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                "Enter valid quantity and price!");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    class SupplierOption {

        int id;
        String name;

        SupplierOption(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return id + " - " + name;
        }
    }

    class ProductOption {

        int id;
        String name;
        double price;
        int quantity;

        ProductOption(int id, String name, double price, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String toString() {
            return id + " - " + name +
                   " (Stock: " + quantity + ")";
        }
    }
}
