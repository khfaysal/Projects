import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/SmartGarage";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, password, role FROM users")) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                users.add(new User(username, password, role));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
        return users;
    }

    public static boolean insertUser(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    public static void saveTransaction(String username, String activity, double amount, String method) {
        String query = "INSERT INTO transactions (username, activity, amount, payment_method, transaction_time) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, activity);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, method);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT type, base_rate FROM vehicles";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                double baseRate = rs.getDouble("base_rate");
                vehicles.add(new Vehicle(type, baseRate));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    public static boolean insertVehicle(String type, double baseRate) {
        String query = "INSERT INTO vehicles (type, base_rate) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, type);
            pstmt.setDouble(2, baseRate);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error inserting vehicle: " + e.getMessage());
            return false;
        }
    }

    public static void deleteAllUsers() {
        String query = "DELETE FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " user(s) deleted successfully.");

        } catch (SQLException e) {
            System.out.println("Error deleting users: " + e.getMessage());
        }
    }

    public static class Vehicle {
        String type;
        double baseRate;

        public Vehicle(String type, double baseRate) {
            this.type = type;
            this.baseRate = baseRate;
        }

        public String getType() {
            return type;
        }

        public double getBaseRate() {
            return baseRate;
        }
    }

    public static class User {
        private final String username;
        private final String password;
        private final String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}
