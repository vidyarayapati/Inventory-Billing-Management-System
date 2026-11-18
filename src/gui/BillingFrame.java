
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import dao.BillingDAO;
import dao.ProductDAO;
import db.DBConnection;
import model.Bill;
import model.BillItem;
import model.Product;

public class BillingFrame extends JFrame {

    private JComboBox<CustomerOption> customerBox;
    private JComboBox<ProductOption> productBox;
    private JTextField quantityField;

    private JTable billTable;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;

    private ArrayList<BillLine> billItems = new ArrayList<>();

    private BillingDAO billingDAO = new BillingDAO();
    private ProductDAO productDAO = new ProductDAO();

    private double grandTotal = 0;

    public BillingFrame() {

        setTitle("VIDYA STORE - Billing");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("GENERATE BILL", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(35, 90, 160));

        add(heading, BorderLayout.NORTH);

        // Customer and Product Selection
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30));

        customerBox = new JComboBox<>();
        productBox = new JComboBox<>();
        quantityField = new JTextField();

        selectionPanel.add(new JLabel("Select Customer:"));
        selectionPanel.add(customerBox);

        selectionPanel.add(new JLabel("Select Product:"));
        selectionPanel.add(productBox);

        selectionPanel.add(new JLabel("Enter Quantity:"));
        selectionPanel.add(quantityField);

        JButton addItemButton = new JButton("Add Item");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(selectionPanel, BorderLayout.CENTER);
        topPanel.add(addItemButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.CENTER);

        // Bill Table
        tableModel = new DefaultTableModel(
                new String[]{"Product ID", "Product Name",
                        "Price", "Quantity", "Subtotal"}, 0);

        billTable = new JTable(tableModel);
        billTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Bill Items"));

        // Bottom Panel
        totalLabel = new JLabel("Grand Total: Rs. 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton generateButton = new JButton("Generate Bill");
        JButton clearButton = new JButton("Clear Bill");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 15, 20));

        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load customers and products
        loadCustomers();
        loadProducts();

        // Add Item

addItemButton.addActionListener(e -> {

    try {
        CustomerOption customer =
                (CustomerOption) customerBox.getSelectedItem();

        ProductOption product =
                (ProductOption) productBox.getSelectedItem();

        if (customer == null || product == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer and product!");
            return;
        }

        int quantity = Integer.parseInt(
                quantityField.getText().trim());

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Quantity must be greater than zero!");
            return;
        }

        if (quantity > product.quantity) {
            JOptionPane.showMessageDialog(this,
                    "Insufficient stock! Available: "
                            + product.quantity);
            return;
        }

        // Check whether product already exists in bill
        for (int i = 0; i < billItems.size(); i++) {

            BillLine item = billItems.get(i);

            if (item.productId == product.id) {

                // Update existing quantity
                item.quantity = quantity;
                item.subtotal = item.price * quantity;

                // Update table row
                tableModel.setValueAt(quantity, i, 3);
                tableModel.setValueAt(item.subtotal, i, 4);

                // Recalculate grand total
                grandTotal = 0;

                for (BillLine billItem : billItems) {
                    grandTotal += billItem.subtotal;
                }

                totalLabel.setText(
                        String.format("Grand Total: Rs. %.2f",
                                grandTotal));

                quantityField.setText("");

                JOptionPane.showMessageDialog(this,
                        "Product quantity updated successfully!");

                return;
            }
        }

        // Add new product if it is not already in bill
        double subtotal = product.price * quantity;

        billItems.add(new BillLine(
                product.id,
                product.name,
                product.price,
                quantity,
                subtotal));

        tableModel.addRow(new Object[]{
                product.id,
                product.name,
                product.price,
                quantity,
                subtotal
        });

        grandTotal += subtotal;

        totalLabel.setText(
                String.format("Grand Total: Rs. %.2f",
                        grandTotal));

        quantityField.setText("");

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(this,
                "Enter a valid quantity!");
    }
});


        // Generate Bill
        generateButton.addActionListener(e -> {

            CustomerOption customer =
                    (CustomerOption) customerBox.getSelectedItem();

            if (customer == null || billItems.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please select a customer and add products!");
                return;
            }

            // Check current stock before generating bill
            for (BillLine item : billItems) {

                Product currentProduct =
                        productDAO.getProductById(item.productId);

                if (currentProduct == null
                        || currentProduct.getQuantity() < item.quantity) {

                    JOptionPane.showMessageDialog(this,
                            "Insufficient stock for " + item.name
                                    + ". Refresh and try again!");
                    loadProducts();
                    return;
                }
            }

            Bill bill = new Bill(
                    0,
                    customer.id,
                    grandTotal,
                    "");

            int billId = billingDAO.createBill(bill);

            if (billId == -1) {

                JOptionPane.showMessageDialog(this,
                        "Failed to create bill!");
                return;
            }

            boolean success = true;

            for (BillLine item : billItems) {

                BillItem billItem = new BillItem(
                        0,
                        billId,
                        item.productId,
                        item.quantity,
                        item.price,
                        item.subtotal);

                if (!billingDAO.addBillItem(billItem)) {
                    success = false;
                    break;
                }

                if (!productDAO.updateStock(
                        item.productId, item.quantity)) {

                    success = false;
                    break;
                }
            }

            if (success) {

                StringBuilder receipt = new StringBuilder();

                receipt.append("       VIDYA STORE\n");
                receipt.append("   INVENTORY & BILLING SYSTEM\n");
                receipt.append("--------------------------------\n");
                receipt.append("Bill ID: ").append(billId).append("\n");
                receipt.append("Customer: ").append(customer.name).append("\n");
                receipt.append("--------------------------------\n");

                for (BillLine item : billItems) {

                    receipt.append(item.name)
                            .append("  x ")
                            .append(item.quantity)
                            .append("  = Rs. ")
                            .append(String.format("%.2f", item.subtotal))
                            .append("\n");
                }

                receipt.append("--------------------------------\n");
                receipt.append("Grand Total: Rs. ")
                        .append(String.format("%.2f", grandTotal))
                        .append("\n");
                receipt.append("--------------------------------\n");
                receipt.append("       THANK YOU!\n");

                JTextArea receiptArea = new JTextArea(
                        receipt.toString());

                receiptArea.setEditable(false);
                receiptArea.setFont(
                        new Font("Monospaced", Font.PLAIN, 14));

                JOptionPane.showMessageDialog(this,
                        new JScrollPane(receiptArea),
                        "Bill Receipt",
                        JOptionPane.INFORMATION_MESSAGE);

                clearBill();
                loadProducts();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Bill processing was incomplete. "
                        + "Please check the database before retrying. "
                        + "Bill ID: " + billId);
            }
        });

        // Clear Bill
        clearButton.addActionListener(e -> clearBill());

        // Back
        backButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Load customers from database
    private void loadCustomers() {

        String sql = "SELECT customer_id, customer_name FROM customers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                customerBox.addItem(new CustomerOption(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name")));
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Unable to load customers: " + e.getMessage());
        }
    }

    // Load available products
    private void loadProducts() {

        productBox.removeAllItems();

        String sql = "SELECT * FROM products WHERE quantity > 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                productBox.addItem(new ProductOption(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Unable to load products: " + e.getMessage());
        }
    }

    // Clear bill details
    private void clearBill() {

        billItems.clear();
        tableModel.setRowCount(0);

        grandTotal = 0;

        totalLabel.setText("Grand Total: Rs. 0.00");
        quantityField.setText("");
    }

    // Customer details for dropdown
    private static class CustomerOption {

        int id;
        String name;

        CustomerOption(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return id + " - " + name;
        }
    }

    // Product details for dropdown
    private static class ProductOption {

        int id;
        String name;
        double price;
        int quantity;

        ProductOption(int id, String name,
                double price, int quantity) {

            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String toString() {
            return id + " - " + name
                    + " (Rs. " + price
                    + ", Stock: " + quantity + ")";
        }
    }

    // Temporary bill item details
    private static class BillLine {

        int productId;
        String name;
        double price;
        int quantity;
        double subtotal;

        BillLine(int productId, String name,
                double price, int quantity, double subtotal) {

            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingFrame());
    }
}
