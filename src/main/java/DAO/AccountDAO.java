package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Function to check if the username is already in use.
    public static boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM account WHERE username = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username); // Bind the username to the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // this gives the count of rows
                    return count > 0; // true if there's at least one match
                }
            }

        } catch (SQLException e) {
            logError("Error while checking if username exists: " + username, e);
        }

        // If there's an error or no result, assume it's not taken
        return false;
    }

    // Adding a new account to the DB... hopefully, this works.
    public static Account insertAccount(Account account) {
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getUsername()); // Bind username
            stmt.setString(2, account.getPassword()); // Bind password

            int rowsAffected = stmt.executeUpdate(); // Run the query
            if (rowsAffected > 0) {
                // Okay, account was added, now we fetch the generated key
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1); // the ID of the new account
                        account.setAccount_id(id); // update the object
                    }
                }
            }
            return account;

        } catch (SQLException e) {
            logError("Failed to insert account: " + account.getUsername(), e);
        }

        return null; // Uh-oh, something went wrong
    }

    // Get an account based on username and password. (Basic login, I guess?)
    public static Account getAccountByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username); // Bind username
            stmt.setString(2, password); // Bind password

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create an account object with the data from the DB
                    return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            logError("Could not retrieve account for username: " + username, e);
        }

        return null; // Couldn't find the account (or maybe an error happened)
    }

    // Check if an account exists based on the ID
    public static boolean isAccountExists(int accountId) {
        String query = "SELECT COUNT(*) FROM account WHERE account_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accountId); // Bind the account ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // true if the account exists
                }
            }

        } catch (SQLException e) {
            logError("Failed to check if account exists with ID: " + accountId, e);
        }

        return false; // Couldn't find the account or an error occurred
    }

    // Just a generic error logger, nothing fancy here.
    private static void logError(String message, Exception exception) {
        System.err.println("[ERROR] " + message); // Print the error
        exception.printStackTrace(); // Dump the stack trace (messy but useful)
    }
}
