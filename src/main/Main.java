package main;
import java.util.Scanner;

import java.util.ArrayList;
import dao.ProductDAO;
import model.Product;
import dao.CustomerDAO;
import model.Customer;
import dao.BillingDAO;
import model.Bill;
import model.BillItem;
import dao.AdminDAO;
import dao.PurchaseDAO;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		ProductDAO productDAO = new ProductDAO();
		CustomerDAO customerDAO = new CustomerDAO();
		BillingDAO billingDAO = new BillingDAO();
		AdminDAO adminDAO = new AdminDAO();
		PurchaseDAO purchaseDAO = new PurchaseDAO();
		System.out.println("================================");
		System.out.println("          ADMIN LOGIN");
		System.out.println("================================");

		System.out.print("Enter Username: ");
		String username = sc.next();

		System.out.print("Enter Password: ");
		String password = sc.next();

		if (!adminDAO.login(username, password)) {
		    System.out.println("Invalid username or password!");
		    sc.close();
		    return;
		}

		System.out.println("Login successful!");
		int choice;

		do {

	    System.out.println("================================");
	    System.out.println(" INVENTORY AND BILLING SYSTEM");
	    System.out.println("================================");

	    System.out.println("1. Add Product");
	    System.out.println("2. View Products");
	    System.out.println("3. Update Product");
	    System.out.println("4. Delete Product");
	    System.out.println("5. Add Customer");
	    System.out.println("6. View Customers");
	    System.out.println("7. Generate Bill");
	    System.out.println("8. View Bills");
	    System.out.println("9. Search Bill by ID");
	    System.out.println("10. Search Customer by ID");
	    System.out.println("11. Update Customer");
	    System.out.println("12. Delete Customer");
	    System.out.println("13. Search Product by ID");
	    System.out.println("14. Low Stock Products");
	    System.out.println("15. Purchase Stock");
	    System.out.println("16. View Suppliers");
	    System.out.println("17. View Purchase History");
	    System.out.println("18. Add Supplier");
	    System.out.println("19. Update Supplier");
	    System.out.println("20. Delete Supplier");
	    System.out.println("21. Exit");
	    System.out.print("Enter your choice: ");
	    
	    choice = sc.nextInt();
	    switch (choice) {
	    case 1:
	        System.out.print("Enter Product Name: ");
	        String name = sc.next();

	        if (name.trim().isEmpty()) {
	            System.out.println("Product name cannot be empty!");
	            break;
	        }

	        System.out.print("Enter Category: ");
	        String category = sc.next();

	        if (category.trim().isEmpty()) {
	            System.out.println("Product category cannot be empty!");
	            break;
	        }

	        System.out.print("Enter Price: ");
	        double price = sc.nextDouble();

	        if (price <= 0) {
	            System.out.println("Product price must be greater than zero!");
	            break;
	        }

	        System.out.print("Enter Quantity: ");
	        int quantity = sc.nextInt();

	        if (quantity < 0) {
	            System.out.println("Product quantity cannot be negative!");
	            break;
	        }

	        Product product = new Product(0, name, category, price, quantity);

	        if (productDAO.addProduct(product)) {
	            System.out.println("Product added successfully!");
	        } else {
	            System.out.println("Failed to add product!");
	        }

	        break;
	    case 2:

	        System.out.println("----- Product List -----");
	        productDAO.viewProducts();

	        break;
	    case 3:
	        System.out.print("Enter Product ID to Update: ");
	        int updateProductId = sc.nextInt();

	        if (updateProductId <= 0) {
	            System.out.println("Invalid Product ID!");
	            break;
	        }

	        System.out.print("Enter New Price: ");
	        double updatedProductPrice = sc.nextDouble();

	        if (updatedProductPrice <= 0) {
	            System.out.println("Product price must be greater than zero!");
	            break;
	        }

	        System.out.print("Enter New Quantity: ");
	        int updatedProductQuantity = sc.nextInt();

	        if (updatedProductQuantity < 0) {
	            System.out.println("Product quantity cannot be negative!");
	            break;
	        }

	        productDAO.updateProduct(updateProductId,
	                updatedProductPrice,
	                updatedProductQuantity);

	        break;
	    case 4:
	        System.out.print("Enter Product ID to Delete: ");
	        int deleteProductId = sc.nextInt();

	        if (deleteProductId <= 0) {
	            System.out.println("Invalid Product ID!");
	            break;
	        }

	        System.out.print("Are you sure you want to delete this product? (yes/no): ");
	        String confirmProductDelete = sc.next();

	        if (confirmProductDelete.equalsIgnoreCase("yes")) {
	            productDAO.deleteProduct(deleteProductId);
	        } else {
	            System.out.println("Product deletion cancelled!");
	        }

	        break;
	    case 5:

	        System.out.print("Enter Customer Name: ");
	        String customerName = sc.next();
	        if (customerName.trim().isEmpty()) {
	            System.out.println("Customer name cannot be empty!");
	            break;
	        }
	        

	        System.out.print("Enter Phone Number: ");
	        String phone = sc.next();
	        if (!phone.matches("[0-9]{10}")) {
	            System.out.println("Phone number must contain exactly 10 digits!");
	            break;
	        }

	        System.out.print("Enter Email: ");
	        String email = sc.next();
	        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            System.out.println("Please enter a valid email address!");
	            break;
	        }

	        Customer customer = new Customer(0, customerName, phone, email);

	        if (customerDAO.addCustomer(customer)) {
	            System.out.println("Customer added successfully!");
	        } else {
	            System.out.println("Failed to add customer!");
	        }

	        break;
	    case 6:

	        System.out.println("----- Customer List -----");
	        customerDAO.viewCustomers();

	        break;
	    case 7:

	        System.out.print("Enter Customer ID: ");
	        int billCustomerId = sc.nextInt();

	        ArrayList<BillItem> billItems = new ArrayList<>();
	        double totalAmount = 0;

	        while (true) {

	            System.out.print("Enter Product ID (0 to finish): ");
	            int billProductId = sc.nextInt();

	            if (billProductId == 0) {
	                break;
	            }

	            Product selectedProduct = productDAO.getProductById(billProductId);

	            if (selectedProduct == null) {
	                System.out.println("Product not found!");
	                continue;
	            }

	            System.out.println("Product Name: " + selectedProduct.getProductName());
	            System.out.println("Price: " + selectedProduct.getPrice());
	            System.out.println("Available Stock: " + selectedProduct.getQuantity());

	            System.out.print("Enter Quantity: ");
	            int billQuantity = sc.nextInt();

	            if (billQuantity <= 0 || billQuantity > selectedProduct.getQuantity()) {
	                System.out.println("Invalid quantity or insufficient stock!");
	                continue;
	            }

	            double subtotal = selectedProduct.getPrice() * billQuantity;

	            BillItem item = new BillItem(
	                0, 0, billProductId,
	                billQuantity, selectedProduct.getPrice(), subtotal
	            );

	            billItems.add(item);
	            totalAmount += subtotal;

	            System.out.println("Product added to bill!");
	        }

	        if (billItems.isEmpty()) {

	            System.out.println("No products added!");

	        } else {

	            Bill newBill = new Bill(0, billCustomerId, totalAmount, "");

	            int billId = billingDAO.createBill(newBill);

	            if (billId > 0) {

	                boolean success = true;

	                for (BillItem item : billItems) {

	                    BillItem savedItem = new BillItem(
	                        0, billId, item.getProductId(),
	                        item.getQuantity(), item.getPrice(), item.getSubtotal()
	                    );

	                    if (billingDAO.addBillItem(savedItem)) {

	                        if (!productDAO.updateStock(item.getProductId(), item.getQuantity())) {
	                            success = false;
	                        }

	                    } else {
	                        success = false;
	                    }
	                }

	                if (success) {

	                    System.out.println("\n==========================================");
	                    System.out.println("             VIDYA STORE");
	                    System.out.println("          BILL RECEIPT");
	                    System.out.println("==========================================");

	                    System.out.println("Bill ID      : " + billId);
	                    System.out.println("Customer ID  : " + billCustomerId);

	                    System.out.println("------------------------------------------");
	                    System.out.printf("%-15s %-5s %-8s %-8s%n",
	                            "Product", "Qty", "Price", "Total");
	                    System.out.println("------------------------------------------");

	                    for (BillItem item : billItems) {

	                        Product p = productDAO.getProductById(item.getProductId());

	                        String productName = (p != null)
	                                ? p.getProductName() : "Unknown";

	                        System.out.printf("%-15s %-5d %-8.2f %-8.2f%n",
	                                productName,
	                                item.getQuantity(),
	                                item.getPrice(),
	                                item.getSubtotal());
	                    }

	                    System.out.println("------------------------------------------");
	                    System.out.printf("GRAND TOTAL: Rs. %.2f%n", totalAmount);
	                    System.out.println("==========================================");
	                    System.out.println("       THANK YOU! VISIT AGAIN!");
	                    System.out.println("==========================================");

	                } else {
	                    System.out.println("Some bill items or stock updates failed. Please check the database.");
	                }

	            } else {
	                System.out.println("Bill generation failed!");
	            }
	        }

	        break;
	    
	    case 8:

	        System.out.println("----- Bill History -----");
	        billingDAO.viewBills();

	        break;

	    case 9:

	        System.out.print("Enter Bill ID: ");
	        int searchBillId = sc.nextInt();

	        billingDAO.searchBillById(searchBillId);

	        break;
	    case 10:

	        System.out.print("Enter Customer ID: ");
	        int searchCustomerId = sc.nextInt();

	        customerDAO.searchCustomerById(searchCustomerId);

	        break;
	    case 11:
	        System.out.print("Enter Customer ID to Update: ");
	        int updateCustomerId = sc.nextInt();
	        sc.nextLine();

	        if (updateCustomerId <= 0) {
	            System.out.println("Invalid Customer ID!");
	            break;
	        }

	        System.out.print("Enter New Customer Name: ");
	        String updatedCustomerName = sc.nextLine();

	        if (updatedCustomerName.trim().isEmpty()) {
	            System.out.println("Customer name cannot be empty!");
	            break;
	        }

	        System.out.print("Enter New Phone: ");
	        String updatedCustomerPhone = sc.nextLine();

	        if (!updatedCustomerPhone.matches("[0-9]{10}")) {
	            System.out.println("Phone number must contain exactly 10 digits!");
	            break;
	        }

	        System.out.print("Enter New Email: ");
	        String updatedCustomerEmail = sc.nextLine();

	        if (!updatedCustomerEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            System.out.println("Please enter a valid email address!");
	            break;
	        }

	        customerDAO.updateCustomer(updateCustomerId,
	                updatedCustomerName,
	                updatedCustomerPhone,
	                updatedCustomerEmail);

	        break;
	    case 12:
	        System.out.print("Enter Customer ID to Delete: ");
	        int deleteCustomerId = sc.nextInt();

	        if (deleteCustomerId <= 0) {
	            System.out.println("Invalid Customer ID!");
	            break;
	        }

	        System.out.print("Are you sure you want to delete this customer? (yes/no): ");
	        String confirmCustomerDelete = sc.next();

	        if (confirmCustomerDelete.equalsIgnoreCase("yes")) {
	            customerDAO.deleteCustomer(deleteCustomerId);
	        } else {
	            System.out.println("Customer deletion cancelled!");
	        }

	        break;
	    case 13:
	        System.out.print("Enter Product ID to search: ");
	        int searchProductId = sc.nextInt();

	        productDAO.searchProductById(searchProductId);
	        break;
	    case 14:
	        productDAO.viewLowStockProducts();
	        break;
	    case 15:
	        System.out.print("Enter Supplier ID: ");
	        int supplierId = sc.nextInt();
	        if (supplierId <= 0) {
	            System.out.println("Invalid Supplier ID!");
	            break;
	        }

	        System.out.print("Enter Product ID: ");
	        int productId = sc.nextInt();
	        if (productId <= 0) {
	            System.out.println("Invalid Product ID!");
	            break;
	        }

	        System.out.print("Enter Purchase Quantity: ");
	        int purchaseQuantity = sc.nextInt();
	        if (purchaseQuantity <= 0) {
	            System.out.println("Quantity must be greater than zero!");
	            break;
	        }

	        System.out.print("Enter Purchase Price: ");
	        double purchasePrice = sc.nextDouble();
	        if (purchasePrice <= 0) {
	            System.out.println("Purchase price must be greater than zero!");
	            break;
	        }

	        purchaseDAO.addPurchase(supplierId, productId, purchaseQuantity, purchasePrice);

	        break;
	    case 16:
	        purchaseDAO.viewSuppliers();
	        break;
	    case 17:
	        purchaseDAO.viewPurchaseHistory();
	        break;
	    case 18:
	    	sc.nextLine();
	        System.out.print("Enter Supplier Name: ");
	        String supplierName = sc.nextLine();
	        if (supplierName.trim().isEmpty()) {
	            System.out.println("Supplier name cannot be empty!");
	            break;
	        }

	        System.out.print("Enter Phone: ");
	        String supplierPhone = sc.nextLine();
	        if (!supplierPhone.matches("[0-9]{10}")) {
	            System.out.println("Phone number must contain exactly 10 digits!");
	            break;
	        }

	        System.out.print("Enter Email: ");
	        String supplierEmail = sc.nextLine();
	        if (!supplierEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            System.out.println("Please enter a valid email address!");
	            break;
	        }

	        System.out.print("Enter City: ");
	        String supplierCity = sc.nextLine();
	        if (supplierCity.trim().isEmpty()) {
	            System.out.println("City cannot be empty!");
	            break;
	        }

	        System.out.print("Enter Address: ");
	        String supplierAddress = sc.nextLine();
	        if (supplierAddress.trim().isEmpty()) {
	            System.out.println("Address cannot be empty!");
	            break;
	        } 

	        purchaseDAO.addSupplier(supplierName, supplierPhone,
	                supplierEmail, supplierCity, supplierAddress);

	        break;
	    case 19:
	        System.out.print("Enter Supplier ID to Update: ");
	        int updateSupplierId = sc.nextInt();
	        sc.nextLine();
	        if (updateSupplierId <= 0) {
	            System.out.println("Invalid Supplier ID!");
	            break;
	        }

	        System.out.print("Enter New Phone: ");
	        String updatedSupplierPhone = sc.nextLine();
	        if (!updatedSupplierPhone.matches("[0-9]{10}")) {
	            System.out.println("Phone number must contain exactly 10 digits!");
	            break;
	        }

	        System.out.print("Enter New Email: ");
	        String updatedSupplierEmail = sc.nextLine();
	        if (!updatedSupplierEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            System.out.println("Please enter a valid email address!");
	            break;
	        }

	        System.out.print("Enter New City: ");
	        String updatedSupplierCity = sc.nextLine();
	        if (updatedSupplierCity.trim().isEmpty()) {
	            System.out.println("City cannot be empty!");
	            break;
	        }

	        System.out.print("Enter New Address: ");
	        String updatedSupplierAddress = sc.nextLine();
	        if (updatedSupplierAddress.trim().isEmpty()) {
	            System.out.println("Address cannot be empty!");
	            break;
	        }

	        purchaseDAO.updateSupplier(updateSupplierId,
	                updatedSupplierPhone,
	                updatedSupplierEmail,
	                updatedSupplierCity,
	                updatedSupplierAddress);

	        break;
	    case 20:
	        System.out.print("Enter Supplier ID to Delete: ");
	        int deleteSupplierId = sc.nextInt();

	        if (deleteSupplierId <= 0) {
	            System.out.println("Invalid Supplier ID!");
	            break;
	        }

	        System.out.print("Are you sure you want to delete this supplier? (yes/no): ");
	        String confirmDelete = sc.next();

	        if (confirmDelete.equalsIgnoreCase("yes")) {
	            purchaseDAO.deleteSupplier(deleteSupplierId);
	        } else {
	            System.out.println("Supplier deletion cancelled!");
	        }

	        break;

	    case 21:
	        System.out.println("Thank you for using our system!");
	        break;
	        
	    default:
	        System.out.println("Invalid choice! Please try again.");

	}

	    } while (choice != 21);

	    sc.close();

	}
}
