package hangman;

import java.sql.*;

// Use JDBC to connect to your database and run queries

public class DatabaseManager {

    public DatabaseManager() {

    }

    public static boolean checkUserAvailability(String username) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("[System]: Opened database successfully");

            String query = "SELECT 1 FROM UserInfo WHERE username = ?";

            PreparedStatement pstmt = c.prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return false;
    }


}