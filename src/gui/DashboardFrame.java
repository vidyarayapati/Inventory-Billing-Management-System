
package gui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("VIDYA STORE - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(35, 90, 160));
        header.setPreferredSize(new Dimension(900, 80));

        JLabel title = new JLabel("VIDYA STORE - INVENTORY MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(Color.WHITE);

        header.add(title);
        add(header, BorderLayout.NORTH);

        // Main Dashboard
        JPanel dashboard = new JPanel();
        dashboard.setLayout(new GridLayout(3, 3, 20, 20));
        dashboard.setBackground(new Color(240, 244, 248));
        dashboard.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));

        String[] options = {
            "Products",
            "Customers",
            "Generate Bill",
            "View Bills",
            "Suppliers",
            "Purchases",
            "Purchase History",
            "Low Stock Products",
            "Logout"
        };

        for (String option : options) {

            JButton button = new JButton(option);
            button.setFont(new Font("Arial", Font.BOLD, 17));
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(35, 90, 160));
            button.setFocusPainted(false);

            dashboard.add(button);

            button.addActionListener(e -> {

                if (option.equals("Logout")) {

                    dispose();
                    new LoginFrame();

                } else if (option.equals("Products")) {

                    new ProductsFrame();

                } else if (option.equals("Customers")) {

                    new CustomerFrame();

                } else if (option.equals("Generate Bill")) {

                    new BillingFrame();

                } else if (option.equals("View Bills")) {

                    new BillsFrame();
                } else if (option.equals("Suppliers")) {

                    new SuppliersFrame();
                } else if (option.equals("Purchases")) {

                    new PurchasesFrame();

                } else if (option.equals("Purchase History")) {

                    new PurchaseHistoryFrame();

                } else if (option.equals("Low Stock Products")) {

                    new LowStockFrame();

                } else {

               
                    

                

                    JOptionPane.showMessageDialog(this,
                            option + " section selected!");

                }
            });
        }

        add(dashboard, BorderLayout.CENTER);

        setVisible(true);
    }

}
