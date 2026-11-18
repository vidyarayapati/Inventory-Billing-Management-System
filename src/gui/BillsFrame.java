
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import db.DBConnection;

public class BillsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public BillsFrame() {

        setTitle("View Bills");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("BILL HISTORY", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Bill ID", "Customer ID", "Customer Name",
                         "Total Amount", "Bill Date"}, 0);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        JPanel searchPanel = new JPanel();

        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchPanel.add(new JLabel("Bill ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Dashboard");
        mainPanel.add(backButton, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchBill());
        refreshButton.addActionListener(e -> loadBills());

        backButton.addActionListener(e -> dispose());

        add(mainPanel);

        loadBills();

        setVisible(true);
    }

    private void loadBills() {

        model.setRowCount(0);

        String sql = "SELECT b.bill_id, b.customer_id, " +
                     "c.customer_name, b.total_amount, b.bill_date " +
                     "FROM bills b JOIN customers c " +
                     "ON b.customer_id = c.customer_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("bill_id"),
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getDouble("total_amount"),
                    rs.getString("bill_date")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchBill() {

        try {
            int billId = Integer.parseInt(searchField.getText());

            model.setRowCount(0);

            String sql = "SELECT b.bill_id, b.customer_id, " +
                         "c.customer_name, b.total_amount, b.bill_date " +
                         "FROM bills b JOIN customers c " +
                         "ON b.customer_id = c.customer_id " +
                         "WHERE b.bill_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, billId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt("bill_id"),
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getDouble("total_amount"),
                            rs.getString("bill_date")
                        });
                    }
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Bill not found!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Bill ID!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
