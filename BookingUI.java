/**
 * BookingUI.java - GUI for booking tickets
 * Allows users to select ticket type, quantity, and view total fare
 * Demonstrates exception handling and validation
 */
import javax.swing.*;
import java.awt.*;

public class BookingUI extends JFrame {
    private CoreSystem coreSystem;
    private DashboardUI dashboardUI;
    private JComboBox<String> ticketComboBox;
    private JTextField quantityField;
    private JLabel totalFareLabel;
    private JLabel selectedTicketLabel;
    
    // Constructor
    public BookingUI(CoreSystem coreSystem, DashboardUI dashboardUI) {
        this.coreSystem = coreSystem;
        this.dashboardUI = dashboardUI;
        initializeUI();
    }
    
    // Initialize the UI components
    private void initializeUI() {
        setTitle("Book Ticket");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top panel for title
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel for booking form
        JPanel centerPanel = createBookingPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = createButtonPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // Create top panel with title
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.setBackground(new Color(230, 230, 250));
        
        JLabel titleLabel = new JLabel("Book Your Ticket", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 0, 139));
        
        panel.add(titleLabel);
        return panel;
    }
    
    // Create booking form panel
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Ticket Type Selection
        panel.add(new JLabel("Select Ticket Type:"));
        ticketComboBox = new JComboBox<>();
        populateTicketTypes();
        ticketComboBox.addActionListener(e -> updateTotalFare());
        panel.add(ticketComboBox);
        
        // Selected Ticket Details
        panel.add(new JLabel("Selected Ticket:"));
        selectedTicketLabel = new JLabel("Please select a ticket");
        selectedTicketLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        panel.add(selectedTicketLabel);
        
        // Quantity Input
        panel.add(new JLabel("Number of Tickets:"));
        quantityField = new JTextField("1");
        quantityField.addActionListener(e -> updateTotalFare());
        panel.add(quantityField);
        
        // Total Fare Display
        panel.add(new JLabel("Total Fare:"));
        totalFareLabel = new JLabel("$0.00");
        totalFareLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalFareLabel.setForeground(new Color(0, 128, 0));
        panel.add(totalFareLabel);
        
        // Calculate Button
        JButton calculateButton = new JButton("Calculate Fare");
        calculateButton.addActionListener(e -> updateTotalFare());
        panel.add(calculateButton);
        
        // Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        
        return panel;
    }
    
    // Create button panel
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Book Button
        JButton bookButton = new JButton("Book Ticket");
        bookButton.setFont(new Font("Arial", Font.BOLD, 12));
        bookButton.setBackground(new Color(0, 128, 0));
        bookButton.setForeground(Color.WHITE);
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.addActionListener(e -> bookTicket());
        panel.add(bookButton);
        
        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());
        panel.add(cancelButton);
        
        return panel;
    }
    
    // Populate ticket types in combo box
    private void populateTicketTypes() {
        for (Ticket ticket : coreSystem.getAvailableTickets()) {
            ticketComboBox.addItem(ticket.getTicketId() + " - " + ticket.getTicketType() + " ($" + ticket.getPrice() + ")");
        }
    }
    
    // Update total fare based on selection
    private void updateTotalFare() {
        try {
            int selectedIndex = ticketComboBox.getSelectedIndex();
            if (selectedIndex == -1) {
                totalFareLabel.setText("$0.00");
                selectedTicketLabel.setText("Please select a ticket");
                return;
            }
            
            Ticket selectedTicket = coreSystem.getAvailableTickets().get(selectedIndex);
            selectedTicketLabel.setText(selectedTicket.toString());
            
            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                totalFareLabel.setText("$0.00");
                return;
            }
            
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                totalFareLabel.setText("$0.00");
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double totalFare = selectedTicket.calculateFare(quantity);
            totalFareLabel.setText(String.format("$%.2f", totalFare));
            
        } catch (NumberFormatException ex) {
            totalFareLabel.setText("$0.00");
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Book the ticket
    private void bookTicket() {
        try {
            int selectedIndex = ticketComboBox.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a ticket type!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the number of tickets!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (quantity > 10) {
                JOptionPane.showMessageDialog(this, "Maximum 10 tickets allowed per booking!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Ticket selectedTicket = coreSystem.getAvailableTickets().get(selectedIndex);
            String bookingId = coreSystem.createBooking(selectedTicket.getTicketId(), quantity);
            
            if (bookingId != null) {
                double totalFare = selectedTicket.calculateFare(quantity);
                String message = "Booking Successful!\n\n" +
                               "Booking ID: " + bookingId + "\n" +
                               "Ticket: " + selectedTicket.toString() + "\n" +
                               "Quantity: " + quantity + "\n" +
                               "Total Fare: $" + String.format("%.2f", totalFare);
                
                JOptionPane.showMessageDialog(this, message, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
                
                // Update dashboard statistics
                dashboardUI.updateStats();
                
                // Clear form for next booking
                clearForm();
                
                // Close window after successful booking
                this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Clear the form
    private void clearForm() {
        ticketComboBox.setSelectedIndex(0);
        quantityField.setText("1");
        totalFareLabel.setText("$0.00");
        selectedTicketLabel.setText("Please select a ticket");
    }
}
