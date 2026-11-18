package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import model.Customer;

public class CustomerDAO {
	// Add a new customer
	public boolean addCustomer(Customer customer) {

	    String sql = "INSERT INTO customers "
	            + "(customer_name, phone, email) VALUES (?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, customer.getCustomerName());
	        ps.setString(2, customer.getPhone());
	        ps.setString(3, customer.getEmail());

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Display all customers
	public void viewCustomers() {

	    String sql = "SELECT * FROM customers";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            System.out.println("Customer ID: "
	                    + rs.getInt("customer_id"));

	            System.out.println("Customer Name: "
	                    + rs.getString("customer_name"));

	            System.out.println("Phone: "
	                    + rs.getString("phone"));

	            System.out.println("Email: "
	                    + rs.getString("email"));

	            System.out.println("-------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Update customer details
	public boolean updateCustomer(int customerId, String name, String phone, String email) {

	    String sql = "UPDATE customers SET customer_name = ?, phone = ?, email = ? "
	               + "WHERE customer_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, name);
	        ps.setString(2, phone);
	        ps.setString(3, email);
	        ps.setInt(4, customerId);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Delete a customer
	public boolean deleteCustomer(int customerId) {

	    String sql = "DELETE FROM customers WHERE customer_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, customerId);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Search customer by ID
	public void searchCustomerById(int customerId) {

	    String sql = "SELECT * FROM customers WHERE customer_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, customerId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            System.out.println("\n===== CUSTOMER DETAILS =====");
	            System.out.println("Customer ID: " + rs.getInt("customer_id"));
	            System.out.println("Customer Name: " + rs.getString("customer_name"));
	            System.out.println("Phone: " + rs.getString("phone"));
	            System.out.println("Email: " + rs.getString("email"));

	        } else {
	            System.out.println("Customer not found!");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
