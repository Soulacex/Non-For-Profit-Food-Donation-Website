package com.ufund.api.ufundapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeedsTest {
    private Needs need;

    @BeforeEach
    public void setUp() {
        // Setup
        need = new Needs("Beans", 2.0, "Canned" , 4);
    }

    @Test
    public void testGetName() {
        // Invoke & Analyze
        assertEquals("Beans", need.getName());
    }

    @Test
    public void testGetCost() {
        // Invoke & Analyze
        assertEquals(2.0, need.getCost());
    }

    @Test
    public void testGetType() {
        // Invoke & Analyze
        assertEquals("Canned", need.getType());
    }

    @Test
    public void testGetQuantity() {
        // Invoke & Analyze
        assertEquals(4, need.getQuantity());
    }
    
    @Test
    public void testSetName() {
        // Setup & Invoke
        need.setName("O.J.");

        // Analyze
        assertEquals("O.J.", need.getName());
    }

    @Test
    public void testSetCost() {
        // Setup & Invoke
        need.setCost(2.5);

        // Analyze
        assertEquals(2.5, need.getCost());
    }

    @Test
    public void testSetType() {
        // Setup & Invoke
        need.setType("Fresh");

        // Analyze
        assertEquals("Fresh", need.getType());
    }

    @Test
    public void testSetQuantity() {
        // Setup & Invoke
        need.setQuantity(6);

        // Analyze
        assertEquals(6, need.getQuantity());
    }

    @Test
    public void testEqualsTrue() {
        // Setup & Invoke
        Needs object = new Needs("Beans", 2.0, "Canned" , 4);

        // Analyze
        assertTrue(need.equals(object));
    }

    @Test 
    public void testEqualsFalse() {
        // Setup & Invoke
        Needs object = new Needs("Pepper", 0.5, "Spice", 10);

        // Analyze
        assertFalse(need.equals(object));
    }

    @Test
    public void testOtherObject(){
        //Setup & Invoke
        String test = "hi";
        
        //Analyze
        assertFalse(need.equals(test));
    }

    @Test
    public void testNull(){
        //Setup & Invoke
        Needs object = null;
        
        //Analyze
        assertFalse(need.equals(object));
    }
}