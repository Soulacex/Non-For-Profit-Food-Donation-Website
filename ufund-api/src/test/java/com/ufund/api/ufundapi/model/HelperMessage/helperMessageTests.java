package com.ufund.api.ufundapi.model.HelperMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class helperMessageTests {
    private HelperMessage helperMessage;

    @BeforeEach
    public void setUp() {
        // Setup
        helperMessage = new HelperMessage(0, "Name Test", "Message Test");
    }

    @Test
    public void testGetId(){
        // Invoke & Analyze
        assertEquals(0, helperMessage.getId());
    }

    @Test
    public void testGetName(){
        // Invoke & Analyze
        assertEquals("Name Test", helperMessage.getName());
    }
    @Test
    public void testGetMessage(){
        // Invoke & Analyze
        assertEquals("Message Test", helperMessage.getMessage());
    }


    
}
