package com.ufund.api.ufundapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SupportedNeedsTests {
    SupportedNeed supportNeed;
    @BeforeEach
    public void setUp() {
        // Setup
        supportNeed = new SupportedNeed("username", "Burger", 4);
    }

    @Test
    public void getUsername(){
        // Invoke & Analyze
        assertEquals("username", supportNeed.getUsername());
    }

    @Test
    public void getSupportedNeedName(){
        // Invoke & Analyze
        assertEquals("Burger", supportNeed.getSupportedNeedName());
    }

    @Test
    public void getSupportedNeedQuantity(){
        // Invoke & Analyze
        assertEquals(4, supportNeed.getSupportedNeedQuantity());
    }

    @Test
    public void setQuantity(){
        // Invoke & Analyze
        supportNeed.setQuantity(5);
        assertEquals(5, supportNeed.getSupportedNeedQuantity());
    }

    @Test
    public void setSupportedNeedQuantity(){
        // Invoke & Analyze
        supportNeed.setSupportedNeedQuantity(5);
        assertEquals(5, supportNeed.getSupportedNeedQuantity());
    }
}
