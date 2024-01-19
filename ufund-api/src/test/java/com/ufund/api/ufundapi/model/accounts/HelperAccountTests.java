package com.ufund.api.ufundapi.model.accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.FundingBasket;

public class HelperAccountTests {
    private HelperAccount helper;

    @BeforeEach
    public void setUp() {
        helper = new HelperAccount("Helper");
    }
    
    @Test
    public void HelperhelperType() {
        // Setup
        AccountType expected = AccountType.Helper;

        // Invoke
        AccountType actual = helper.getType();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void HelperhelperUsername() {
        // Setup
        String expected = "Helper";        
        
        // Invoke
        String actual = helper.getUsername();
        
        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void HelperhelperFundingBasket() {
        // Invoke
        FundingBasket basket = helper.getBasket();

        //Analyze
        assertNotNull(basket);
    }
}
