import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ticketsystem";
    private static final String USER = "ticket_user";
    private static final String PASSWORD = "password123";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createUserTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "username VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL)";
        String createTicketTable = "CREATE TABLE IF NOT EXISTS Tickets (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createTicketTable);
            System.out.println("Database initialized with user and ticket tables.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}
