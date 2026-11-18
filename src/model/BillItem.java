package model;

public class BillItem {

    private int billItemId;
    private int billId;
    private int productId;
    private int quantity;
    private double price;
    private double subtotal;

    public BillItem(int billItemId, int billId, int productId,
                    int quantity, double price, double subtotal) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public int getBillItemId() {
        return billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
