package com.ufund.api.ufundapi.model.accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountsTests {
    private Accounts accounts;

    @BeforeEach
    public void setUp() {
        accounts = new Accounts();
    }

    @Test
    public void CreateHelperAccount() {
        // Setup
        String username = "Helper";

        // Invoke
        HelperAccount helper = accounts.createHelper(username);
        String actual = helper.getUsername();
        
        // Analyze
        assertEquals(username, actual);
    }

    @Test
    public void CreateDuplicateHelperAccount() {
        // Setup
        String username = "Helper";
        accounts.createHelper(username);

        // Invoke
        HelperAccount actual = accounts.createHelper(username);
        
        // Analyze
        assertNull(actual);
    }

    @Test
    public void GetAnAccount() {
        // Setup
        String username = "Helper";
        accounts.createHelper(username);

        // Invoke
        UserAccount account = accounts.getAccount(username);
        String actual = account.getUsername();
        
        // Analyze
        assertEquals(username, actual);
    }

    @Test
    public void GetNonexistentAccount() {
        // Setup
        String username = "Helper";

        // Invoke
        UserAccount actual = accounts.getAccount(username);
        
        // Analyze
        assertNull(actual);
    }

    @Test
    public void AccountsManagerCreated() {
        // Setup
        AccountType expected = AccountType.Manager;
        String managerUsername = "admin";

        // Invoke
        UserAccount manager = accounts.getAccount(managerUsername);
        AccountType actual = manager.getType();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getAllAccounts() {
        // Setup
        int expected = 2;
        accounts.createHelper("helper");

        // Invoke
        UserAccount[] allAccounts = accounts.getAllAccounts();
        int actual = allAccounts.length;

        // Analyze
        assertEquals(expected, actual);
    }
}
