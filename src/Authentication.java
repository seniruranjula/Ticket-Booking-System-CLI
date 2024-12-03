import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Authentication {
    private static final Logger logger = LogManager.getLogger();

    public static void registerUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            logger.info("User registered successfully.");
        } catch (SQLException e) {
            logger.warning("Failed to register user: " + e.getMessage());
        }
    }

    public static boolean login(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT username FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("User " + username + " logged in successfully.");
                return true;
            }
        } catch (SQLException e) {
            logger.warning("Failed to log in: " + e.getMessage());
        }
        logger.warning("Invalid username or password.");
        try {
            Thread.sleep(100);  // 2000 milliseconds = 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle interruption
            logger.log(Level.WARNING, "Thread was interrupted during sleep", e);
        }
        return false;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }
}
