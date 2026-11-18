package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import model.Bill;
import model.BillItem;

public class BillingDAO {
	// Create a new bill
	public int createBill(Bill bill) {

	    String sql = "INSERT INTO bills (customer_id, total_amount) VALUES (?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql,
	                 java.sql.Statement.RETURN_GENERATED_KEYS)) {

	        ps.setInt(1, bill.getCustomerId());
	        ps.setDouble(2, bill.getTotalAmount());

	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();

	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}
	// Add products to a bill
	public boolean addBillItem(BillItem item) {

	    String sql = "INSERT INTO bill_items "
	            + "(bill_id, product_id, quantity, price, subtotal) "
	            + "VALUES (?, ?, ?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, item.getBillId());
	        ps.setInt(2, item.getProductId());
	        ps.setInt(3, item.getQuantity());
	        ps.setDouble(4, item.getPrice());
	        ps.setDouble(5, item.getSubtotal());

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// View all bills
	public void viewBills() {

	    String sql = "SELECT * FROM bills";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            System.out.println("Bill ID: " + rs.getInt("bill_id"));
	            System.out.println("Customer ID: " + rs.getInt("customer_id"));
	            System.out.println("Total Amount: " + rs.getDouble("total_amount"));
	            System.out.println("Bill Date: " + rs.getTimestamp("bill_date"));
	            System.out.println("--------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Search bill by Bill ID
	public void searchBillById(int billId) {

	    String sql = "SELECT b.bill_id, b.customer_id, b.total_amount, " +
	                 "b.bill_date, p.product_name, bi.quantity, " +
	                 "bi.price, bi.subtotal " +
	                 "FROM bills b " +
	                 "JOIN bill_items bi ON b.bill_id = bi.bill_id " +
	                 "JOIN products p ON bi.product_id = p.product_id " +
	                 "WHERE b.bill_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, billId);

	        ResultSet rs = ps.executeQuery();

	        boolean found = false;
	        double totalAmount = 0;

	        while (rs.next()) {

	            if (!found) {
	            	totalAmount = rs.getDouble("total_amount");
	                System.out.println("\n========== BILL DETAILS ==========");
	                System.out.println("Bill ID: " + rs.getInt("bill_id"));
	                System.out.println("Customer ID: " + rs.getInt("customer_id"));
	                System.out.println("Bill Date: " + rs.getTimestamp("bill_date"));
	                System.out.println("----------------------------------");
	                found = true;
	            }

	            System.out.println("Product: " + rs.getString("product_name"));
	            System.out.println("Quantity: " + rs.getInt("quantity"));
	            System.out.println("Price: " + rs.getDouble("price"));
	            System.out.println("Subtotal: " + rs.getDouble("subtotal"));
	            System.out.println("----------------------------------");
	        }

	        if (found) {
	            System.out.println("Total Amount: " + totalAmount);
	        } else {
	            System.out.println("Bill not found!");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}