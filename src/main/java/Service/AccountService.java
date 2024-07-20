package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor for a AccountService when a AccountDAO is provided.
     * @param AccountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    /**
     * Use the AccountDAO to retrieve all accounts.
     *
     * @return all accounts
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }
    /**
     * Use the AccountDAO to persist an Account. The given Account will not have an id provided.
     *
     * @param account an Account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    /**
     * Use the AccountDAO to search for an Account using username and password.
     * The given Account will not have an id provided.
     *
     * @param account an Account object.
     * @return The found account if the login is successful.
     */
    public Account SearchAccount(Account account) {
        return accountDAO.findAccount(account);
    }
}
