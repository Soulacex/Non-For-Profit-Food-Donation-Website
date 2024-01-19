package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.accounts.HelperAccount;

public interface AccountsDAO {
    
    /**
     * Checks if username is unique then creates a new Helper user based and adds it to the accountList.
     * @param username The username for the account.
     * @return Returns new HelperAccount created.
     *         Returns null if username already exists.
     */
    HelperAccount createHelper(String username) throws IOException;

    /**
    * Function that returns all the accounts that has been created.
    * @return Returns ArrayList of all the accounts created.
    **/
    String[] getAllAccounts() throws IOException;
}
