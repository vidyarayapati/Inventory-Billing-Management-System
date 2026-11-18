package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DBConnection;

public class PurchaseDAO {
	public boolean addPurchase(int supplierId, int productId, int quantity, double price) {

	    double subtotal = quantity * price;

	    Connection con = DBConnection.getConnection();

	    try {
	        con.setAutoCommit(false);
	        String purchaseSQL = "INSERT INTO purchases (supplier_id, total_amount) VALUES (?, ?)";

	        PreparedStatement ps = con.prepareStatement(
	                purchaseSQL, Statement.RETURN_GENERATED_KEYS);

	        ps.setInt(1, supplierId);
	        ps.setDouble(2, subtotal);

	        ps.executeUpdate();
	        ResultSet rs = ps.getGeneratedKeys();

	        int purchaseId = 0;

	        if (rs.next()) {
	            purchaseId = rs.getInt(1);
	        }
	        String itemSQL = "INSERT INTO purchase_items "
	                + "(purchase_id, product_id, quantity, price, subtotal) "
	                + "VALUES (?, ?, ?, ?, ?)";

	        PreparedStatement itemPS = con.prepareStatement(itemSQL);

	        itemPS.setInt(1, purchaseId);
	        itemPS.setInt(2, productId);
	        itemPS.setInt(3, quantity);
	        itemPS.setDouble(4, price);
	        itemPS.setDouble(5, subtotal);

	        itemPS.executeUpdate();
	        String stockSQL = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";

	        PreparedStatement stockPS = con.prepareStatement(stockSQL);

	        stockPS.setInt(1, quantity);
	        stockPS.setInt(2, productId);

	        int rows = stockPS.executeUpdate();

	        if (rows == 0) {
	            throw new SQLException("Product not found!");
	        }
	        con.commit();

	        System.out.println("Purchase added successfully!");
	        System.out.println("Product stock updated successfully!");

	        return true;

	    } catch (SQLException e) {
	        try {
	            con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	        e.printStackTrace();
	    }
	    finally {
	        try {
	            if (con != null) {
	                con.setAutoCommit(true);
	                con.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return false;
	}
	// Add viewSuppliers() method here
	public void viewSuppliers() {

	    String sql = "SELECT * FROM suppliers";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        System.out.println("\n========== SUPPLIER DETAILS ==========");

	        while (rs.next()) {
	            System.out.println("Supplier ID   : " + rs.getInt("supplier_id"));
	            System.out.println("Supplier Name : " + rs.getString("supplier_name"));
	            System.out.println("Phone         : " + rs.getString("phone"));
	            System.out.println("Email         : " + rs.getString("email"));
	            System.out.println("City          : " + rs.getString("city"));
	            System.out.println("Address       : " + rs.getString("address"));
	            System.out.println("--------------------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public void viewPurchaseHistory() {

	    String sql = "SELECT p.purchase_id, s.supplier_name, "
	            + "p.purchase_date, p.total_amount "
	            + "FROM purchases p "
	            + "JOIN suppliers s ON p.supplier_id = s.supplier_id "
	            + "ORDER BY p.purchase_id";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        System.out.println("\n========== PURCHASE HISTORY ==========");

	        while (rs.next()) {

	            System.out.println("Purchase ID   : " + rs.getInt("purchase_id"));
	            System.out.println("Supplier Name : " + rs.getString("supplier_name"));
	            System.out.println("Purchase Date : " + rs.getTimestamp("purchase_date"));
	            System.out.println("Total Amount  : Rs. " + rs.getDouble("total_amount"));

	            System.out.println("--------------------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public boolean addSupplier(String name, String phone, String email,
            String city, String address) {

String sql = "INSERT INTO suppliers "
+ "(supplier_name, phone, email, city, address) "
+ "VALUES (?, ?, ?, ?, ?)";

try (Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql)) {

ps.setString(1, name);
ps.setString(2, phone);
ps.setString(3, email);
ps.setString(4, city);
ps.setString(5, address);

int rows = ps.executeUpdate();

if (rows > 0) {
System.out.println("Supplier added successfully!");
return true;
}

} catch (SQLException e) {
e.printStackTrace();
}

return false;
}
	public boolean updateSupplier(int supplierId, String phone,
	        String email, String city, String address) {

	    String sql = "UPDATE suppliers SET phone = ?, email = ?, "
	            + "city = ?, address = ? WHERE supplier_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, phone);
	        ps.setString(2, email);
	        ps.setString(3, city);
	        ps.setString(4, address);
	        ps.setInt(5, supplierId);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("Supplier updated successfully!");
	            return true;
	        } else {
	            System.out.println("Supplier not found!");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	public void deleteSupplier(int supplierId) {

	    String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, supplierId);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("Supplier deleted successfully!");
	        } else {
	            System.out.println("Supplier ID not found!");
	        }

	    } catch (SQLException e) {
	        System.out.println("Cannot delete supplier. Purchase records may exist.");
	    }
	}

}
