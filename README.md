# Inventory and Billing Management System

## Project Overview

The Inventory and Billing Management System is a Java-based desktop application designed to manage products, customers, suppliers, purchases, and billing operations.

The application uses Java Swing for its graphical user interface and MySQL for database management. JDBC is used to connect Java with the MySQL database.

## Technologies Used

- **Programming Language:** Core Java
- **GUI:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** Eclipse
- **JDK:** Java 21

## Features

### 1. Admin Login
- Secure admin login.
- Invalid login validation.

### 2. Product Management
- Add new products.
- View available products.
- Update product details.
- Delete products.
- Search products by ID.

### 3. Customer Management
- Add customers.
- View customer details.
- Update customer information.
- Delete customers.
- Search customers by ID.

### 4. Billing Management
- Generate customer bills.
- Add products to bills.
- Calculate total bill amount.
- View billing history.
- Search bills by ID.
- Update product stock after billing.

### 5. Supplier Management
- Add suppliers.
- View supplier details.
- Update supplier information.
- Delete suppliers.

### 6. Purchase Management
- Record product purchases.
- Update inventory stock after purchases.
- View purchase history.

### 7. Low Stock Management
- Identify products with low stock.
- View products with quantity of 5 or less.

## Database Tables

The project uses the following MySQL tables:

1. admins
2. products
3. customers
4. bills
5. bill_items
6. suppliers
7. purchases
8. purchase_items

## Project Structure

```text
InventoryBillingSystem/
│
├── src/
│   ├── dao/
│   ├── db/
│   ├── gui/
│   ├── main/
│   ├── model/
│   └── service/
│
├── .classpath
├── .project
├── .gitignore
└── README.md
```

## How to Run the Project

1. Install JDK 21.
2. Install MySQL Server and MySQL Workbench.
3. Create a database named `inventory_billing`.
4. Create the required database tables.
5. Configure the MySQL JDBC Connector in Eclipse.
6. Set the following Windows environment variables:
   - `DB_USER` – Your MySQL username.
   - `DB_PASSWORD` – Your MySQL password.
7. Import the project into Eclipse.
8. Run `main.Main.java`.

## Author

**Vidya Rayapati**

GitHub: [vidyarayapati](https://github.com/vidyarayapati)
