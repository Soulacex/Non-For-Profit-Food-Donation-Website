package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.accounts.HelperAccount;

/**
 * Implements the functionality for JSON file-based peristance for Account
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author 7G FundsInHighPlaces
 */
@Component
public class AccountsFileDAO implements AccountsDAO {
    private static final Logger LOG = Logger.getLogger(AccountsFileDAO.class.getName());
    ArrayList<String> accounts;   // Provides a local cache of the account objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Account
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    /**
     * Creates am Account File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AccountsFileDAO(@Value("${Accounts.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the accounts from the file
        if (!accounts.contains("admin")) {
            accounts.add("admin"); // adds an admin account by default
            save();
        }
    }

    /**
     * Generates an array of {@linkplain String accounts} from the tree map for any
     * {@linkplain String accounts} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain String accounts}
     * in the arraylist
     * 
     * @return  The array of {@link String accounts}, may be empty
     */
    private String[] getAccountsArray() {
        ArrayList<String> accountArrayList = new ArrayList<>();

        for (String account : accounts) {
            accountArrayList.add(account);
        }

        String[] accountArray = new String[accountArrayList.size()];
        accountArrayList.toArray(accountArray);
        return accountArray;
    }

    /**
     * Saves the {@linkplain String account} from the arraylist into the file as an array of JSON objects
     * 
     * @return true if the {@link String account} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        String[] accountArray = getAccountsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),accountArray);
        return true;
    }
    
    /**
     * Loads {@linkplain String account} from the JSON file into the arraylist
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        accounts = new ArrayList<>();

        // Deserializes the JSON objects from the file into an array of accounts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from  xthe file
        String[] accountArray = objectMapper.readValue(new File(filename),String[].class);

        // Add each account to the arraylist and keep track of the greatest id
        for (String account : accountArray) {
            accounts.add(account);
        }
        return true;
    }

    /**
     * Creates and saves a {@linkplain HelperAccount helperAccount}
     * 
     * @param username {@linkplain String username} for a object to be created and saved
     *
     * @return new {@link HelperAccount helperAccount} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    @Override
    public HelperAccount createHelper(String username) throws IOException{
        synchronized (accounts) {
            if (accounts.contains(username) != false) {
                return null;
            }

            HelperAccount newHelper = new HelperAccount(username);
            accounts.add(username);
            save();
            return newHelper;
        }
    }

    /**
     * Retrieves all {@linkplain String account}
     * 
     * @return An array of {@link String account} String ids
     * 
     * @throws IOException if an issue with underlying storage
     */
    @Override
    public String[] getAllAccounts() {
        synchronized (accounts) {
            return getAccountsArray();
        }
    }
}
