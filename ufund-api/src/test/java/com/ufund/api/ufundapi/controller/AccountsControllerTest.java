package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import com.ufund.api.ufundapi.model.accounts.HelperAccount;
import com.ufund.api.ufundapi.persistence.AccountsDAO;

public class AccountsControllerTest {
    private AccountsController accountsController;
    private AccountsDAO mockAccountsDao;
    /**
     * Before each test, generate a new AccountsController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockAccountsDao = mock(AccountsDAO.class);
        accountsController = new AccountsController(mockAccountsDao);
    }

    @Test
    public void createHelper() throws IOException{
        // Setup
        HelperAccount user = new HelperAccount("Sam");
        when(mockAccountsDao.createHelper(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<HelperAccount> responseEntity = accountsController.createHelper(user); 

        // Analyze
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void createHelperConflict() throws IOException{
        // Setup
        HelperAccount user = new HelperAccount("Sam");
        when(mockAccountsDao.createHelper(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<HelperAccount> responseEntity = accountsController.createHelper(user); 

        // Analyze
        assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
    }

    @Test
    public void createHelperIOExcpetion() throws IOException{
        // Setup
        HelperAccount user = new HelperAccount("Sam");
        String username = user.getUsername();
        doThrow(new IOException ()).when(mockAccountsDao).createHelper(username);

        // Invoke
        ResponseEntity<HelperAccount> responseEntity = accountsController.createHelper(user); 

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }

    @Test
    public void getAllAccounts() throws IOException{
        // Setup
        String[] accounts= new String[2];
        accounts[0] = "Susan";
        accounts[1] = "Dan";
     
        when(mockAccountsDao.getAllAccounts()).thenReturn(accounts);

        // Invoke
        ResponseEntity<String[]> responseEntity = accountsController.getAllAccounts();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllNeedsIOException() throws IOException {
           // Setup
        String[] accounts= new String[2];
        accounts[0] = "Susan";
        accounts[1] = "Dan";
     
        doThrow(new IOException()).when(mockAccountsDao).getAllAccounts();

        // Invoke
        ResponseEntity<String[]> responseEntity = accountsController.getAllAccounts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
