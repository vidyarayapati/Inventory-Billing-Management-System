
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import db.DBConnection;

public class PurchaseHistoryFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public PurchaseHistoryFrame() {

        setTitle("Purchase History");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("PURCHASE HISTORY", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Purchase ID", "Supplier ID", "Product ID",
                         "Quantity", "Purchase Price", "Purchase Date"}, 0);

        table = new JTable(model);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadHistory());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);

        loadHistory();

        setVisible(true);
    }

    private void loadHistory() {

        model.setRowCount(0);

        String sql = "SELECT p.purchase_id, p.supplier_id, " +
                "pi.product_id, pi.quantity, pi.price, " +
                "p.purchase_date " +
                "FROM purchases p " +
                "JOIN purchase_items pi " +
                "ON p.purchase_id = pi.purchase_id " +
                "ORDER BY p.purchase_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("purchase_id"),
                    rs.getInt("supplier_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("purchase_date")
                });

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

