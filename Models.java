/**
 * Models.java - Consolidated data models
 * Combines User, Ticket, PremiumTicket, and Booking classes
 * Maintains all functionality while reducing file count
 */

// User class
class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean validateLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "User: " + fullName + " (" + username + ")";
    }
}

// Ticket class
class Ticket {
    private String ticketId;
    private String ticketType;
    private double price;
    
    public Ticket(String ticketId, String ticketType, double price) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.price = price;
    }
    
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double calculateFare(int quantity) {
        return price * quantity;
    }
    
    @Override
    public String toString() {
        return "Ticket ID: " + ticketId + ", Type: " + ticketType + ", Price: $" + price;
    }
}

// PremiumTicket class
class PremiumTicket extends Ticket {
    private double premiumMultiplier;
    
    public PremiumTicket(String ticketId, String ticketType, double price, double premiumMultiplier) {
        super(ticketId, ticketType, price);
        this.premiumMultiplier = premiumMultiplier;
    }
    
    public double getPremiumMultiplier() { return premiumMultiplier; }
    public void setPremiumMultiplier(double premiumMultiplier) { this.premiumMultiplier = premiumMultiplier; }
    
    @Override
    public double calculateFare(int quantity) {
        return super.calculateFare(quantity) * premiumMultiplier;
    }
    
    @Override
    public String toString() {
        return "Premium " + super.toString() + ", Multiplier: " + premiumMultiplier;
    }
}

// Booking class
class Booking {
    private String bookingId;
    private User user;
    private Ticket ticket;
    private int quantity;
    private double totalFare;
    private String bookingDate;
    
    public Booking(String bookingId, User user, Ticket ticket, int quantity) {
        this.bookingId = bookingId;
        this.user = user;
        this.ticket = ticket;
        this.quantity = quantity;
        this.totalFare = ticket.calculateFare(quantity);
        this.bookingDate = java.time.LocalDate.now().toString();
    }
    
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        this.totalFare = ticket.calculateFare(quantity);
    }
    public double getTotalFare() { return totalFare; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    
    @Override
    public String toString() {
        return "Booking ID: " + bookingId + 
               "\nUser: " + user.getFullName() +
               "\nTicket: " + ticket.toString() +
               "\nQuantity: " + quantity +
               "\nTotal Fare: $" + totalFare +
               "\nBooking Date: " + bookingDate;
    }
}
