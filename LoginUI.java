/**
 * LoginUI.java - GUI for user login and registration
 * Uses JFrame, JPanel, JButton, JLabel, JTextField, JPasswordField
 * Demonstrates event handling with ActionListener
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame {
    private CoreSystem coreSystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPanel loginPanel;
    private JPanel registerPanel;
    
    // Constructor
    public LoginUI(CoreSystem coreSystem) {
        this.coreSystem = coreSystem;
        initializeUI();
    }
    
    // Initialize the UI components
    private void initializeUI() {
        setTitle("Online Ticket Booking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setMinimumSize(new Dimension(400, 300));
        setMaximumSize(new Dimension(600, 450));
        setResizable(true);
        setLocationRelativeTo(null);
        
        // Create tabbed pane for Login and Register
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create Login Panel
        loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);
        
        // Create Register Panel
        registerPanel = createRegisterPanel();
        tabbedPane.addTab("Register", registerPanel);
        
        add(tabbedPane);
    }
    
    // Create Login Panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    // Create Register Panel
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        fullNameField = new JTextField(15);
        panel.add(fullNameField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField regUsernameField = new JTextField(15);
        panel.add(regUsernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JPasswordField regPasswordField = new JPasswordField(15);
        panel.add(regPasswordField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField emailField = new JTextField(15);
        panel.add(emailField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String fullName = fullNameField.getText().trim();
            String username = regUsernameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String email = emailField.getText().trim();
            
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (coreSystem.registerUser(username, password, fullName, email)) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields
                fullNameField.setText("");
                regUsernameField.setText("");
                regPasswordField.setText("");
                emailField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            fullNameField.setText("");
            regUsernameField.setText("");
            regPasswordField.setText("");
            emailField.setText("");
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    // ActionListener for Login Button
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginUI.this, "Please enter username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (coreSystem.loginUser(username, password)) {
                JOptionPane.showMessageDialog(LoginUI.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Open Dashboard
                DashboardUI dashboard = new DashboardUI(coreSystem);
                dashboard.setVisible(true);
                LoginUI.this.dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(LoginUI.this, "Invalid username or password!\n\nPlease make sure:\n1. You have registered first\n2. Username and password are correct", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CoreSystem system = new CoreSystem();
            LoginUI login = new LoginUI(system);
            login.setVisible(true);
        });
    }
}
