package service;

import dao.BillingDAO;
import model.Bill;
import model.BillItem;

public class BillingService {

    private BillingDAO billingDAO = new BillingDAO();
 // Generate a new bill
    public int generateBill(Bill bill) {

        return billingDAO.createBill(bill);

    }
 // Add an item to the bill
    public boolean addItemToBill(BillItem item) {

        return billingDAO.addBillItem(item);

    }
 // View all bills
    public void viewAllBills() {

        billingDAO.viewBills();

    }
}