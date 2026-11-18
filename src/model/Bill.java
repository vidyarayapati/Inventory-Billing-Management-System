package model;

public class Bill {

    private int billId;
    private int customerId;
    private double totalAmount;
    private String billDate;

    public Bill(int billId, int customerId, double totalAmount, String billDate) {
        this.billId = billId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
    }

    public int getBillId() {
        return billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getBillDate() {
        return billDate;
    }
}