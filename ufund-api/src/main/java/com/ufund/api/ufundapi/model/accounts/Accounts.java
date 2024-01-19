package com.ufund.api.ufundapi.model.accounts;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.controller.AccountsController;

@Repository
/**
 * Represents the accounts for users and managers
 * 
 * @author Kelvin Ng, Mason Zeng, Charles von Goins II, Sam Pepperman
 */
public class Accounts {
    private static final Logger LOG = Logger.getLogger(AccountsController.class.getName());
    private ArrayList<UserAccount> accountList;

    @JsonProperty("username") private String username;

    public Accounts() {
        accountList = new ArrayList<>();
        accountList.add(new ManagerAccount());
    }

    /**
     * Checks if username is unique then creates a new Helper user based and adds it to the accountList.
     * @param username The username for the account.
     * @return Returns new HelperAccount created.
     *         Returns null if username already exists.
     */
    public HelperAccount createHelper(@JsonProperty("username") String username) {
        for (UserAccount userLogin : accountList) {
            if (userLogin.getUsername().equals(username)) {
                return null;
            }
        }
        HelperAccount newHelper = new HelperAccount(username);
        accountList.add(newHelper);
        return newHelper;
    }

    /**
    * Since each account has a unique username, searching for it in the array will return the specific account requested
    * @param username The username of the account being searched for.
    *
    * @return Returns UserAccount requested.
    *         Returns null if no account with the username exists.
    */
    public UserAccount getAccount(String username) {
        for (UserAccount userAccount : accountList) {
            if (userAccount.getUsername().equals(username)) {
                return userAccount;
            }
        }
        return null;
    }

    /**
    * Function that returns all the accounts that has been created.
    * @return Returns ArrayList of all the accounts created.
    **/
    public UserAccount[] getAllAccounts() {
        UserAccount[] accountArray = new UserAccount[accountList.size()];
        accountArray = accountList.toArray(accountArray);
        return accountArray;
    }

}
