# Online Ticket Booking System

A complete Java Swing desktop application for booking and managing tickets with user authentication and booking management features.

## Project Team

- Prasannashree B
- Aswathi H  
- Devna N
- Ameena Khatoon

## Features

- User registration and login system
- Dashboard with navigation menu
- Ticket booking with fare calculation
- Ticket cancellation with confirmation
- View available tickets
- View user booking history
- Real-time statistics display

## Project Structure

### Core Classes
- `Ticket.java` - Base ticket class with encapsulation
- `PremiumTicket.java` - Inherits from Ticket, demonstrates polymorphism
- `User.java` - User management with login validation
- `Booking.java` - Booking information and relationships
- `BookingSystem.java` - Core business logic and data management

### GUI Classes
- `Main.java` - Application entry point
- `LoginUI.java` - Login and registration interface
- `DashboardUI.java` - Main dashboard with navigation
- `BookingUI.java` - Ticket booking interface
- `CancelUI.java` - Ticket cancellation interface

## How to Run

1. Compile all Java files:
   ```bash
   javac *.java
   ```

2. Run the application:
   ```bash
   java Main
   ```

## User Interaction Flow

1. **Login/Register**: Users can create an account or login with existing credentials
2. **Dashboard**: Main menu with access to all features
3. **Book Ticket**: Select ticket type, quantity, and view total fare
4. **Cancel Ticket**: View bookings and cancel with confirmation
5. **View Bookings**: Display all user bookings with details

## OOP Concepts Implemented

### Encapsulation
- All classes have private fields with public getters/setters
- Data validation in setter methods
- Private helper methods in classes

### Inheritance
- `PremiumTicket` extends `Ticket` base class
- Inherits properties and methods from parent class

### Polymorphism
- `calculateFare()` method overridden in `PremiumTicket`
- Dynamic method binding for fare calculation

### Abstraction
- Service methods in `BookingSystem` hide implementation details
- Clean separation between GUI and business logic

## Exception Handling

- Input validation for empty fields
- Number format exception handling
- User-friendly error messages with JOptionPane
- Graceful error recovery

## GUI Components Used

- `JFrame` - Main application windows
- `JPanel` - Container for components
- `JButton` - Action buttons with event handling
- `JLabel` - Text display
- `JTextField` - Text input
- `JPasswordField` - Secure password input
- `JComboBox` - Dropdown selections
- `JTabbedPane` - Tabbed interface
- `JOptionPane` - Dialog boxes

## Layout Managers

- `BorderLayout` - Main window layout
- `GridLayout` - Form layouts
- `FlowLayout` - Button arrangements

## Data Storage

- Uses `ArrayList` for in-memory storage
- No external database required
- Data persists during application runtime

## System Requirements

- Java 8 or higher
- Swing library (included with JDK)
- Windows/Linux/MacOS with GUI support
