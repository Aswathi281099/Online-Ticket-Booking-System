/**
 * DashboardUI.java - Main dashboard with navigation buttons
 * Provides access to all booking system features
 * Uses BorderLayout and GridLayout for layout management
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardUI extends JFrame {
    private CoreSystem coreSystem;
    private JLabel welcomeLabel;
    private JLabel statsLabel;
    
    // Constructor
    public DashboardUI(CoreSystem coreSystem) {
        this.coreSystem = coreSystem;
        initializeUI();
    }
    
    // Initialize the UI components
    private void initializeUI() {
        setTitle("Online Ticket Booking System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top panel for welcome message
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel for buttons
        JPanel centerPanel = createButtonPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel for statistics
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // Create top panel with welcome message
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 230, 250));
        
        User currentUser = coreSystem.getCurrentUser();
        String welcomeText = "Welcome, " + currentUser.getFullName() + "!";
        welcomeLabel = new JLabel(welcomeText, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(0, 0, 139));
        
        panel.add(welcomeLabel);
        return panel;
    }
    
    // Create center panel with action buttons
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // View Tickets Button
        JButton viewTicketsButton = new JButton("View Available Tickets");
        viewTicketsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewTicketsButton.addActionListener(e -> viewTickets());
        panel.add(viewTicketsButton);
        
        // Book Ticket Button
        JButton bookTicketButton = new JButton("Book Ticket");
        bookTicketButton.setFont(new Font("Arial", Font.PLAIN, 14));
        bookTicketButton.addActionListener(e -> bookTicket());
        panel.add(bookTicketButton);
        
        // Cancel Ticket Button
        JButton cancelTicketButton = new JButton("Cancel Ticket");
        cancelTicketButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelTicketButton.addActionListener(e -> cancelTicket());
        panel.add(cancelTicketButton);
        
        // View Bookings Button
        JButton viewBookingsButton = new JButton("View My Bookings");
        viewBookingsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewBookingsButton.addActionListener(e -> viewBookings());
        panel.add(viewBookingsButton);
        
        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton);
        
        // Exit Button
        JButton exitButton = new JButton("Exit Application");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);
        
        return panel;
    }
    
    // Create bottom panel with statistics
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));
        
        int totalBookings = coreSystem.getTotalBookings();
        int userBookings = coreSystem.getUserBookings().size();
        String statsText = "Total Bookings: " + totalBookings + " | Your Bookings: " + userBookings;
        statsLabel = new JLabel(statsText, JLabel.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setForeground(new Color(70, 70, 70));
        
        panel.add(statsLabel);
        return panel;
    }
    
    // Method to view available tickets
    private void viewTickets() {
        StringBuilder ticketList = new StringBuilder("Available Tickets:\n\n");
        for (Ticket ticket : coreSystem.getAvailableTickets()) {
            ticketList.append(ticket.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, ticketList.toString(), "Available Tickets", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method to open booking window
    private void bookTicket() {
        BookingUI bookingUI = new BookingUI(coreSystem, this);
        bookingUI.setVisible(true);
    }
    
    // Method to open cancellation window
    private void cancelTicket() {
        CancelUI cancelUI = new CancelUI(coreSystem, this);
        cancelUI.setVisible(true);
    }
    
    // Method to view user bookings
    private void viewBookings() {
        java.util.ArrayList<Booking> userBookings = coreSystem.getUserBookings();
        if (userBookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no bookings yet!", "Your Bookings", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder bookingList = new StringBuilder("Your Bookings:\n\n");
            for (Booking booking : userBookings) {
                bookingList.append(booking.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, bookingList.toString(), "Your Bookings", JOptionPane.INFORMATION_MESSAGE);
        }
        updateStats(); // Refresh statistics
    }
    
    // Method to logout
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            coreSystem.logout();
            LoginUI loginUI = new LoginUI(coreSystem);
            loginUI.setVisible(true);
            this.dispose();
        }
    }
    
    // Update statistics display
    public void updateStats() {
        int totalBookings = coreSystem.getTotalBookings();
        int userBookings = coreSystem.getUserBookings().size();
        String statsText = "Total Bookings: " + totalBookings + " | Your Bookings: " + userBookings;
        statsLabel.setText(statsText);
    }
}
