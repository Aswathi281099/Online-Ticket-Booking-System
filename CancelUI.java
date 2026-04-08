/**
 * CancelUI.java - GUI for ticket cancellation
 * Allows users to view and cancel their bookings
 * Demonstrates exception handling and user confirmation
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CancelUI extends JFrame {
    private CoreSystem coreSystem;
    private DashboardUI dashboardUI;
    private JComboBox<String> bookingComboBox;
    private JLabel bookingDetailsLabel;
    
    // Constructor
    public CancelUI(CoreSystem coreSystem, DashboardUI dashboardUI) {
        this.coreSystem = coreSystem;
        this.dashboardUI = dashboardUI;
        initializeUI();
    }
    
    // Initialize the UI components
    private void initializeUI() {
        setTitle("Cancel Ticket");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top panel for title
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel for cancellation form
        JPanel centerPanel = createCancelPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = createButtonPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Load bookings on initialization
        loadBookings();
    }
    
    // Create top panel with title
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.setBackground(new Color(255, 230, 230));
        
        JLabel titleLabel = new JLabel("Cancel Your Booking", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(139, 0, 0));
        
        panel.add(titleLabel);
        return panel;
    }
    
    // Create cancellation form panel
    private JPanel createCancelPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Booking Selection
        JLabel selectLabel = new JLabel("Select Booking to Cancel:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(selectLabel);
        
        bookingComboBox = new JComboBox<>();
        bookingComboBox.addActionListener(e -> displayBookingDetails());
        panel.add(bookingComboBox);
        
        // Booking Details
        JLabel detailsLabel = new JLabel("Booking Details:");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(detailsLabel);
        
        bookingDetailsLabel = new JLabel("Please select a booking");
        bookingDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        bookingDetailsLabel.setForeground(new Color(70, 70, 70));
        bookingDetailsLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        bookingDetailsLabel.setPreferredSize(new Dimension(400, 150));
        bookingDetailsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        bookingDetailsLabel.setVerticalAlignment(SwingConstants.TOP);
        
        JScrollPane scrollPane = new JScrollPane(bookingDetailsLabel);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        panel.add(scrollPane);
        
        return panel;
    }
    
    // Create button panel
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Cancel Booking Button
        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBookingButton.setBackground(new Color(220, 20, 60));
        cancelBookingButton.setForeground(Color.WHITE);
        cancelBookingButton.setOpaque(true);
        cancelBookingButton.setBorderPainted(false);
        cancelBookingButton.addActionListener(e -> cancelBooking());
        panel.add(cancelBookingButton);
        
        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadBookings());
        panel.add(refreshButton);
        
        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        panel.add(closeButton);
        
        return panel;
    }
    
    // Load user bookings into combo box
    private void loadBookings() {
        bookingComboBox.removeAllItems();
        bookingDetailsLabel.setText("Please select a booking");
        
        ArrayList<Booking> userBookings = coreSystem.getUserBookings();
        if (userBookings.isEmpty()) {
            bookingComboBox.addItem("No bookings available");
            bookingComboBox.setEnabled(false);
        } else {
            bookingComboBox.setEnabled(true);
            for (Booking booking : userBookings) {
                String displayText = booking.getBookingId() + " - " + 
                                   booking.getTicket().getTicketType() + 
                                   " (" + booking.getQuantity() + " tickets)";
                bookingComboBox.addItem(displayText);
            }
        }
    }
    
    // Display selected booking details
    private void displayBookingDetails() {
        int selectedIndex = bookingComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            bookingDetailsLabel.setText("Please select a booking");
            return;
        }
        
        ArrayList<Booking> userBookings = coreSystem.getUserBookings();
        if (selectedIndex >= userBookings.size()) {
            return;
        }
        
        Booking selectedBooking = userBookings.get(selectedIndex);
        String details = "<html><body style='padding: 10px;'>" +
                        "<b>Booking ID:</b> " + selectedBooking.getBookingId() + "<br>" +
                        "<b>User:</b> " + selectedBooking.getUser().getFullName() + "<br>" +
                        "<b>Ticket:</b> " + selectedBooking.getTicket().toString() + "<br>" +
                        "<b>Quantity:</b> " + selectedBooking.getQuantity() + "<br>" +
                        "<b>Total Fare:</b> $" + String.format("%.2f", selectedBooking.getTotalFare()) + "<br>" +
                        "<b>Booking Date:</b> " + selectedBooking.getBookingDate() + "<br><br>" +
                        "<font color='red'><b>WARNING:</b> This action cannot be undone!</font>" +
                        "</body></html>";
        
        bookingDetailsLabel.setText(details);
    }
    
    // Cancel the selected booking
    private void cancelBooking() {
        int selectedIndex = bookingComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ArrayList<Booking> userBookings = coreSystem.getUserBookings();
        if (selectedIndex >= userBookings.size()) {
            JOptionPane.showMessageDialog(this, "Invalid selection!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Booking selectedBooking = userBookings.get(selectedIndex);
        
        // Confirm cancellation
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel this booking?\n\n" +
            "Booking ID: " + selectedBooking.getBookingId() + "\n" +
            "Ticket: " + selectedBooking.getTicket().getTicketType() + "\n" +
            "Quantity: " + selectedBooking.getQuantity() + "\n" +
            "Total Fare: $" + String.format("%.2f", selectedBooking.getTotalFare()),
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = coreSystem.cancelBooking(selectedBooking.getBookingId());
            
            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "Booking cancelled successfully!\n\n" +
                    "Booking ID: " + selectedBooking.getBookingId() + "\n" +
                    "Refund Amount: $" + String.format("%.2f", selectedBooking.getTotalFare()),
                    "Cancellation Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Update dashboard statistics
                dashboardUI.updateStats();
                
                // Refresh the bookings list
                loadBookings();
                
                // Close window after successful cancellation
                this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to cancel booking! Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
