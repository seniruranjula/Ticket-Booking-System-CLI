import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Initialize the database connection
        DatabaseConnection.initializeDatabase();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticket System");

        // Registration or Login prompt
        System.out.print("Do you have an account? (yes/no): ");
        String hasAccount = scanner.nextLine().trim().toLowerCase();
        if (hasAccount.equals("no")) {
            System.out.print("Enter a username to register: ");
            String newUsername = scanner.nextLine();
            System.out.print("Enter a password: ");
            String newPassword = scanner.nextLine();
            Authentication.registerUser(newUsername, newPassword);
            System.out.println("Registration successful. Please log in.");
        }

        // Login prompt
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            loggedIn = Authentication.login(username, password);
            if (!loggedIn) {
                System.out.println("Login failed. Try again.");
            }
        }

        try {
            Thread.sleep(100); // Short sleep for a smoother user experience
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            logger.log(Level.WARNING, "Thread was interrupted during sleep", e);
        }

        // Load system configuration
        Configuration config = new Configuration();
        config.loadConfiguration();
        TicketPool ticketPool = new TicketPool(config.maxTicketCapacity);

        // Start Vendor and Customer threads
        List<Thread> vendors = new ArrayList<>();
        List<Thread> customers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Thread vendor = new Thread(new Vendor(ticketPool, config.ticketReleaseRate));
            vendors.add(vendor);
            vendor.start();
            logger.info("Vendor thread started.");
        }

        for (int i = 0; i < 2; i++) {
            Thread customer = new Thread(new Customer(ticketPool, config.customerRetrievalRate));
            customers.add(customer);
            customer.start();
            logger.info("Customer thread started.");
        }

        // User command to stop the program
        System.out.println("Type 'stop' to end the program.");
        while (!scanner.nextLine().equalsIgnoreCase("stop")) {
            System.out.println("Waiting for stop command...");
        }

        // Interrupt and stop all threads
        vendors.forEach(Thread::interrupt);
        customers.forEach(Thread::interrupt);

        // Wait for threads to finish
        try {
            for (Thread vendor : vendors) {
                vendor.join(5000); // Timeout after 5 seconds
            }
            for (Thread customer : customers) {
                customer.join(5000); // Timeout after 5 seconds
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error while waiting for threads to finish", e);
            Thread.currentThread().interrupt(); // Restore interrupt status
        }

        // Cleanup
        scanner.close();
        logger.info("System stopped gracefully.");
    }
}
