
package gui;

import javax.swing.*;
import java.awt.*;
import dao.AdminDAO;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Inventory Billing System - Admin Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 244, 248));

        JLabel title = new JLabel("VIDYA STORE");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(145, 35, 250, 40);
        panel.add(title);

        JLabel subtitle = new JLabel("Inventory & Billing Management");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setBounds(125, 80, 280, 25);
        panel.add(subtitle);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(80, 145, 100, 30);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(180, 145, 220, 30);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 195, 100, 30);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(180, 195, 220, 30);
        panel.add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(180, 255, 220, 35);
        loginButton.setBackground(new Color(35, 90, 160));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            AdminDAO adminDAO = new AdminDAO();

            if (adminDAO.login(username, password)) {

                JOptionPane.showMessageDialog(this, "Login Successful!");

                dispose();
                new DashboardFrame();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Invalid username or password!");

            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

