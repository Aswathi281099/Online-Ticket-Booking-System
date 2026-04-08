/**
 * CoreSystem.java - Consolidated business logic and database operations
 * Combines BookingSystem and DatabaseManager functionality
 * Maintains all features while reducing file count
 */

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class CoreSystem {
    private ArrayList<User> users;
    private ArrayList<Ticket> tickets;
    private ArrayList<Booking> bookings;
    private User currentUser;
    private static final String USERS_FILE = "users.dat";
    private static final String BOOKINGS_FILE = "bookings.dat";
    
    public CoreSystem() {
        users = loadUsers();
        tickets = new ArrayList<>();
        bookings = loadBookings(users, tickets);
        currentUser = null;
        initializeTickets();
        System.out.println("DEBUG: CoreSystem initialized with " + users.size() + " users and " + bookings.size() + " bookings");
    }
    
    private void initializeTickets() {
        tickets.add(new Ticket("T001", "Bus", 25.0));
        tickets.add(new Ticket("T002", "Train", 45.0));
        tickets.add(new Ticket("T003", "Movie", 15.0));
        tickets.add(new PremiumTicket("T004", "Premium Bus", 35.0, 1.5));
        tickets.add(new PremiumTicket("T005", "Premium Train", 65.0, 1.5));
    }
    
    // User Management
    public boolean registerUser(String username, String password, String fullName, String email) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password, fullName, email));
        saveUsers(users);
        System.out.println("DEBUG: User registered and saved: " + username);
        return true;
    }
    
    public boolean loginUser(String username, String password) {
        System.out.println("DEBUG: Attempting login for username: " + username);
        System.out.println("DEBUG: Total users in system: " + users.size());
        
        for (User user : users) {
            System.out.println("DEBUG: Checking user: " + user.getUsername());
            if (user.validateLogin(username, password)) {
                currentUser = user;
                System.out.println("DEBUG: Login successful for: " + username);
                return true;
            }
        }
        System.out.println("DEBUG: Login failed for: " + username);
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Ticket Management
    public ArrayList<Ticket> getAvailableTickets() {
        return tickets;
    }
    
    public Ticket getTicketById(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }
    
    // Booking Management
    public String createBooking(String ticketId, int quantity) {
        if (currentUser == null) {
            return null;
        }
        
        Ticket ticket = getTicketById(ticketId);
        if (ticket == null) {
            return null;
        }
        
        String bookingId = "B" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking booking = new Booking(bookingId, currentUser, ticket, quantity);
        bookings.add(booking);
        saveBookings(bookings);
        System.out.println("DEBUG: Booking created and saved: " + bookingId);
        return bookingId;
    }
    
    public boolean cancelBooking(String bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(bookingId)) {
                bookings.remove(i);
                saveBookings(bookings);
                System.out.println("DEBUG: Booking cancelled and saved: " + bookingId);
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Booking> getUserBookings() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().getUsername().equals(currentUser.getUsername())) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }
    
    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }
    
    public Booking getBookingById(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }
    
    // Utility Methods
    public ArrayList<String> getTicketTypes() {
        ArrayList<String> types = new ArrayList<>();
        for (Ticket ticket : tickets) {
            types.add(ticket.getTicketType() + " - $" + ticket.getPrice());
        }
        return types;
    }
    
    public int getTotalBookings() {
        return bookings.size();
    }
    
    public int getTotalUsers() {
        return users.size();
    }
    
    // Database Operations
    private void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(user.getUsername() + "|" + 
                              user.getPassword() + "|" + 
                              user.getFullName() + "|" + 
                              user.getEmail());
            }
            System.out.println("DEBUG: Saved " + users.size() + " users to file");
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    private ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        if (!file.exists()) {
            System.out.println("DEBUG: No users file found, starting with empty user list");
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3]);
                    users.add(user);
                }
            }
            System.out.println("DEBUG: Loaded " + users.size() + " users from file");
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
    
    private void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : bookings) {
                writer.println(booking.getBookingId() + "|" + 
                              booking.getUser().getUsername() + "|" + 
                              booking.getTicket().getTicketId() + "|" + 
                              booking.getTicket().getTicketType() + "|" + 
                              booking.getTicket().getPrice() + "|" + 
                              booking.getQuantity() + "|" + 
                              booking.getTotalFare() + "|" + 
                              booking.getBookingDate());
            }
            System.out.println("DEBUG: Saved " + bookings.size() + " bookings to file");
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
    
    private ArrayList<Booking> loadBookings(ArrayList<User> users, ArrayList<Ticket> tickets) {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKINGS_FILE);
        
        if (!file.exists()) {
            System.out.println("DEBUG: No bookings file found, starting with empty booking list");
            return bookings;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    // Find the user
                    User bookingUser = null;
                    for (User user : users) {
                        if (user.getUsername().equals(parts[1])) {
                            bookingUser = user;
                            break;
                        }
                    }
                    
                    // Find or create the ticket
                    Ticket bookingTicket = null;
                    for (Ticket ticket : tickets) {
                        if (ticket.getTicketId().equals(parts[2])) {
                            bookingTicket = ticket;
                            break;
                        }
                    }
                    
                    if (bookingUser != null && bookingTicket != null) {
                        // Create booking with reconstructed ticket
                        Ticket reconstructedTicket = new Ticket(parts[2], parts[3], Double.parseDouble(parts[4]));
                        Booking booking = new Booking(parts[0], bookingUser, reconstructedTicket, Integer.parseInt(parts[5]));
                        bookings.add(booking);
                    }
                }
            }
            System.out.println("DEBUG: Loaded " + bookings.size() + " bookings from file");
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing booking data: " + e.getMessage());
        }
        
        return bookings;
    }
}
