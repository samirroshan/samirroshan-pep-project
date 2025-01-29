package DAO;
import Model.Account;

import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO 
{

    public static boolean isUsernameTaken(String username) 
    {
        String query = "SELECT COUNT(*) FROM account WHERE username = ?";
        
        try (Connection connection = ConnectionUtil.getConnection();PreparedStatement statement = connection.prepareStatement(query)) 
        {
            
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            System.out.println("Username already exists: " + username); 
                        } else {
                            System.out.println("Username is available: " + username);
                        }
                        return count > 0;
                    }
                    
            }
            
        } 
        catch (SQLException exception) 
        {
            logError("Error checking if username exists: " + username, exception);
        }

        return false;
    }

    public static Account insertAccount(Account account) 
    {
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        
        try (Connection connection = ConnectionUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) 
        {
            
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());

            int rowsInserted = statement.executeUpdate();
            
            if (rowsInserted > 0) 
            {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) 
                {
                    if (generatedKeys.next()) 
                    {
                        account.setAccount_id(generatedKeys.getInt(1));
                    }
                }
            }
            
            return account;
            
        } 
        catch (SQLException exception) 
        {
            logError("Error inserting account: " + account.getUsername(), exception);
        }

        return null;
    }

    public static Account getAccountByUsernameAndPassword(String username, String password) 
    {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        
        try (Connection connection = ConnectionUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) 
        {
            
            statement.setString(1, username);
            statement.setString(2, password);
            
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next()) 
                {
                    return new Account(resultSet.getInt("account_id"), resultSet.getString("username"), resultSet.getString("password"));
                }
            }
            
        } 
        catch (SQLException exception) 
        {
            logError("Error retrieving account for username: " + username, exception);
        }

        return null;
    }

    public static boolean isAccountExists(int accountId) 
    {
        String query = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        
        try (Connection connection = ConnectionUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) 
        {
            
            statement.setInt(1, accountId);
            
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        System.out.println("Account exists for ID: " + accountId);
                    } else {
                        System.out.println("No account found for ID: " + accountId);
                    }
                    return count > 0;
                }
                
            }
            
        } 
        catch (SQLException exception) 
        {
            logError("Error checking if account exists with ID: " + accountId, exception);
        }
        return false;
    }

    private static void logError(String message, Exception exception) 
    {
        System.err.println("ERROR " + message);
        exception.printStackTrace();
    }
}