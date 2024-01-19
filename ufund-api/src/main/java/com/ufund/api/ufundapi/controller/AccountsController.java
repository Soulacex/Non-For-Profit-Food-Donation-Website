package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.accounts.Accounts;
import com.ufund.api.ufundapi.model.accounts.HelperAccount;
import com.ufund.api.ufundapi.persistence.AccountsDAO;

/**
 * Handles the REST API requests for the Account resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/account")
public class AccountsController {
    private static final Logger LOG = Logger.getLogger(AccountsController.class.getName());
    private AccountsDAO accountsDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param accountsDao The {@link accountDAO Account Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public AccountsController(AccountsDAO accountsDao) {
        this.accountsDao = accountsDao;
    }

    /**
     * Creates a {@linkplain HelperAccount account} with the provided account object
     * 
     * @param account - The {@link HelperAccount account} to create
     * 
     * @return ResponseEntity with created {@link HelperAccount account} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link HelperAccount account} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<HelperAccount> createHelper(@RequestBody HelperAccount account) {
        LOG.info("POST /account/createHelper");
        try {
            HelperAccount newHelper = accountsDao.createHelper(account.getUsername());
            if (newHelper != null)
                return new ResponseEntity<HelperAccount>(newHelper, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE,IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Accounts accounts}
     * 
     * @return ResponseEntity with ArrayList of {@link Accounts accounts} objects and HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<String[]> getAllAccounts() {
        LOG.info("GET /accounts");
        try{
            String[] allAccounts = accountsDao.getAllAccounts();
            return new ResponseEntity<String[]>(allAccounts,HttpStatus.OK);
        }
        catch(IOException IOE) {
            LOG.log(Level.SEVERE, IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
