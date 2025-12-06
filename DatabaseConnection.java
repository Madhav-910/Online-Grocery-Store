package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseConnection {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connect();
    }

    // Method to establish a database connection
    private void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            throw e;
        }
    }
    // Method to validate user login
    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // returns true if a matching user is found
        } catch (SQLException e) {
            System.out.println("Login validation failed: " + e.getMessage());
            return false;
        }
    }

    // Method to register a new user
    public boolean registerUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // returns true if the user is registered
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Method to store order details
 // In DatabaseConnection.java

    public boolean storeOrder(String username, double totalPrice, String paymentMethod, List<CartItem> cartItems) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = getConnection();  // Get the database connection
            conn.setAutoCommit(false);  // Start a transaction

            // Insert the order details into the orderdetails table
            String orderQuery = "INSERT INTO orderdetails (username, total_price, payment_method) VALUES (?, ?, ?)";
            orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, username);
            orderStmt.setDouble(2, totalPrice);
            orderStmt.setString(3, paymentMethod);
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // Retrieve the generated order_id
            generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);  // Get the order_id

                // Insert each item in the order_items table with the order_id
                String itemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                itemStmt = conn.prepareStatement(itemQuery);
                for (CartItem item : cartItems) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getId());  // Assuming Product has an ID field
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getProduct().getPrice());
                    itemStmt.addBatch();
                }

                // Execute the batch and check if all items were inserted successfully
                int[] results = itemStmt.executeBatch();
                for (int result : results) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        conn.rollback();  // If any insert failed, roll back the transaction
                        return false;
                    }
                }

                conn.commit();  // Commit the transaction if all items are successfully inserted
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (orderStmt != null) orderStmt.close();
                if (itemStmt != null) itemStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);  // Restore the default auto-commit behavior
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to close the database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }

    // Check if the connection is established
    public boolean isConnected() {
        return connection != null;
    }
}
