package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public static boolean isUsernameTaken(String username)
    {
        return AccountDAO.isUsernameTaken(username);
    }

    public static Account registerAccount(Account account)
    {
        return AccountDAO.insertAccount(account);
    }

    public static Account authenticate(String username, String password){
        return AccountDAO.getAccountByUsernameAndPassword(username, password);
    }

    public static boolean isAccountExists(int accountId)
    {
        return AccountDAO.isAccountExists(accountId);
    }
}
