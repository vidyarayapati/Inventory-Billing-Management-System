package model;

// Customer details
public class Customer {

    private int customerId;
    private String customerName;
    private String phone;
    private String email;
 // Getters and Setters

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
 // Constructor
    public Customer(int customerId, String customerName,
                    String phone, String email) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
    }
    @Override
    public String toString() {
        return "Customer ID: " + customerId
                + "\nCustomer Name: " + customerName
                + "\nPhone: " + phone
                + "\nEmail: " + email;
    }
}