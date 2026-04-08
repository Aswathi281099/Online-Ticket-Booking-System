/**
 * Main.java - Entry point of the Online Ticket Booking System
 * Uses consolidated architecture with Models.java and CoreSystem.java
 * Reduces file count from 11 to 4 Java files
 */
import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        CoreSystem coreSystem = new CoreSystem();
        
        SwingUtilities.invokeLater(() -> {
            try {
                LoginUI loginUI = new LoginUI(coreSystem);
                loginUI.setVisible(true);
                
                System.out.println("========================================");
                System.out.println("  Online Ticket Booking System");
                System.out.println("  (Consolidated Version - 4 files)");
                System.out.println("========================================");
                System.out.println("Developed by:");
                System.out.println("- Prasannashree B");
                System.out.println("- Aswathi H");
                System.out.println("- Devna N");
                System.out.println("- Ameena Khatoon");
                System.out.println("========================================");
                System.out.println("Application started successfully!");
                System.out.println("File count reduced from 11 to 4 Java files");
                System.out.println("========================================");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
