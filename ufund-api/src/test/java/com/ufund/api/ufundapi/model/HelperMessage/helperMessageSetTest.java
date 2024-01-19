package com.ufund.api.ufundapi.model.HelperMessage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class helperMessageSetTest {
    private HelperMessageSet helperMessageSet;

    @BeforeEach
    public void setUp(){
        helperMessageSet = new HelperMessageSet();
    }

    @Test
    public void createMessage() {
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(0,"Test","Test");

        // Invoke
        String actual = message.getMessage();
        String expected = "Test";

        // Analyze
        assertEquals(actual, expected);
    }

    @Test
    public void createMessageInvID() {
        // Setup
        helperMessageSet.createHelperMessage(0,"Test","Test");
        HelperMessage messageDupe = helperMessageSet.createHelperMessage(0,"Test1","Test1");
        // Invoke
        HelperMessage actual = messageDupe;
        // Analyze
        assertEquals(actual, null);
    }

    @Test
    public void createMessageInvName() {
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(0,"","Test");
        // Invoke
        HelperMessage actual = message;
        // Analyze
        assertEquals(actual, null);
    }

    @Test
    public void createHelperMessage() {

    }

    @Test
    public void searchMessage() {
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(0,"Test","");

        // Invoke
        HelperMessage actual = helperMessageSet.searchMessage(0);

        // Analyze
        assertEquals(actual, message);
    }

    @Test
    public void searchMessageInv() {
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(0,"","Test");
        // Invoke
        HelperMessage actual = helperMessageSet.searchMessage(1);
        // Analyze
        assertEquals(actual, message);
    }
    @Test
    public void searchMessageFound() {
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(42, "Test", "Test");

        // Invoke
        HelperMessage actual = helperMessageSet.searchMessage(42);

        // Analyze
        assertEquals(actual, message);
    }

    @Test
    public void searchMessageNotFound(){
        // Setup
        HelperMessage message = helperMessageSet.createHelperMessage(42, "Test", "Test");
        
        //Invoke
        HelperMessage actual = helperMessageSet.searchMessage( 198);

        //Analyze
        assertEquals(actual, null);
    }
}