package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.accounts.HelperAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountsFileDAOTest {
    AccountsFileDAO accountsFileDAO;
    String[] testAccounts;
    ObjectMapper mockObjectMapper;
    
    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupAccountsFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testAccounts = new String[4];
        testAccounts[0] = "admin";
        testAccounts[1] = "helper1";
        testAccounts[2] = "helper2";
        testAccounts[3] = "helper3";

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the account array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),String[].class))
                .thenReturn(testAccounts);
        accountsFileDAO = new AccountsFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetAllAccounts() {
        // Invoke
        String[] accounts = accountsFileDAO.getAllAccounts();

        // Analyze
        assertEquals(accounts.length,testAccounts.length);
        for (int i = 0; i < testAccounts.length;++i)
            assertEquals(accounts[i],testAccounts[i]);
    }

    @Test
    public void testCreateHelper() {
        // Setup
        String username = "helper63";

        // Invoke
        HelperAccount result = assertDoesNotThrow(() -> accountsFileDAO.createHelper(username),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        String actual = username;
        assertEquals(actual,result.getUsername());
    }

    @Test
    public void testCreateHelperDuplicate() {
        // Setup
        String username = "helper1";

        // Invoke
        HelperAccount result = assertDoesNotThrow(() -> accountsFileDAO.createHelper(username),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(String[].class));

        String username = "helper63";

        assertThrows(IOException.class,
                        () -> accountsFileDAO.createHelper(username),
                        "IOException not thrown");
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the HeroFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),String[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new AccountsFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
