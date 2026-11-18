
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import db.DBConnection;

public class LowStockFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public LowStockFrame() {

        setTitle("Low Stock Products");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("LOW STOCK PRODUCTS", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Product ID", "Product Name", "Category",
                         "Price", "Available Quantity"}, 0);

        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadLowStock());

        backButton.addActionListener(e -> dispose());

        add(mainPanel);

        loadLowStock();

        setVisible(true);
    }

    private void loadLowStock() {

        model.setRowCount(0);

        String sql = "SELECT product_id, product_name, category, " +
                     "price, quantity FROM products " +
                     "WHERE quantity <= 5 ORDER BY quantity ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                });

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
