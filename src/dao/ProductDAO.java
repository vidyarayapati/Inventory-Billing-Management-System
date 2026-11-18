package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import model.Product;

public class ProductDAO {
	// Add a new product to the database
	public boolean addProduct(Product product) {

	    String sql = "INSERT INTO products "
	            + "(product_name, category, price, quantity) "
	            + "VALUES (?, ?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, product.getProductName());
	        ps.setString(2, product.getCategory());
	        ps.setDouble(3, product.getPrice());
	        ps.setInt(4, product.getQuantity());

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Display all products
	public void viewProducts() {

	    String sql = "SELECT * FROM products";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            System.out.println("Product ID: " + rs.getInt("product_id"));
	            System.out.println("Product Name: " + rs.getString("product_name"));
	            System.out.println("Category: " + rs.getString("category"));
	            System.out.println("Price: " + rs.getDouble("price"));
	            System.out.println("Quantity: " + rs.getInt("quantity"));

	            System.out.println("-------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Update product price and quantity
	public boolean updateProduct(int productId, double price, int quantity) {

	    String sql = "UPDATE products SET price = ?, quantity = ? "
	               + "WHERE product_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setDouble(1, price);
	        ps.setInt(2, quantity);
	        ps.setInt(3, productId);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Delete a product
	public boolean deleteProduct(int productId) {

	    String sql = "DELETE FROM products WHERE product_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Get product details using Product ID
	public Product getProductById(int productId) {

	    String sql = "SELECT * FROM products WHERE product_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            return new Product(
	                rs.getInt("product_id"),
	                rs.getString("product_name"),
	                rs.getString("category"),
	                rs.getDouble("price"),
	                rs.getInt("quantity")
	            );
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	// Update product stock after billing
	public boolean updateStock(int productId, int quantity) {

	    String sql = "UPDATE products SET quantity = quantity - ? "
	               + "WHERE product_id = ? AND quantity >= ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, quantity);
	        ps.setInt(2, productId);
	        ps.setInt(3, quantity);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Search product by ID
	public void searchProductById(int productId) {

	    String sql = "SELECT * FROM products WHERE product_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, productId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            System.out.println("\n===== PRODUCT DETAILS =====");
	            System.out.println("Product ID: " + rs.getInt("product_id"));
	            System.out.println("Product Name: " + rs.getString("product_name"));
	            System.out.println("Category: " + rs.getString("category"));
	            System.out.println("Price: " + rs.getDouble("price"));
	            System.out.println("Available Stock: " + rs.getInt("quantity"));

	        } else {
	            System.out.println("Product not found!");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// View Low Stock Products
	public void viewLowStockProducts() {

	    String sql = "SELECT * FROM products WHERE quantity <= 5";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        boolean found = false;

	        System.out.println("\n===== LOW STOCK PRODUCTS =====");

	        while (rs.next()) {
	            found = true;

	            System.out.println("Product ID: " + rs.getInt("product_id"));
	            System.out.println("Product Name: " + rs.getString("product_name"));
	            System.out.println("Available Quantity: " + rs.getInt("quantity"));
	            System.out.println("----------------------------");
	        }

	        if (!found) {
	            System.out.println("No low stock products found!");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}