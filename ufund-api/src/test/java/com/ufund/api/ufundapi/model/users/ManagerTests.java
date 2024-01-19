package com.ufund.api.ufundapi.model.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;

public class ManagerTests {
    private Manager manager;

    @BeforeEach
    public void setUp() {
        manager = new Manager(new Cupboard());
    }
    
    @Test
    public void addFoodNeed() {
        // Setup
        String name = "Burger";
        double cost = 5;
        String type = "Perishable";
        Integer quantity = 3;

        // Invoke
        Needs need = manager.addFoodNeed(name, cost, type, quantity);
        String actual = need.getName();
        String expected = "Burger";

        // Analyze
        assertEquals(actual, expected);
    }

    @Test
    public void addFoodNeedDuplicate() {
        // Setup
        String name = "Burger";
        double cost = 5;
        String type = "Perishable";
        Integer quantity = 3;

        // Invoke
        manager.addFoodNeed(name, cost, type, quantity);
        Needs need = manager.addFoodNeed(name, cost, type, quantity);

        // Analyze
        assertNull(need);
    }

    @Test
    public void removeFoodNeed() {
        // Setup
        String name = "Burger";
        double cost = 5;
        String type = "Perishable";
        Integer quantity = 3;

        // Invoke
        manager.addFoodNeed(name, cost, type, quantity);
        boolean removed = manager.removeFoodNeed(name);

        // Analyze
        assertTrue(removed);
    }

    @Test
    public void removeFoodNeedFail() {
        // Setup
        String name = "Burger";

        // Invoke
        boolean removed = manager.removeFoodNeed(name);

        // Analyze
        assertFalse(removed);
    }

    @Test
    public void editFoodNeed() {
        // Setup
        String name = "Burger";
        double cost = 5;
        String type = "Perishable";
        Integer quantity = 3;

        // Invoke
        manager.addFoodNeed(name, cost, type, quantity);
        boolean result = manager.editFoodNeed(name, 10, null, -1);

        // Analyze
        assertTrue(result);
    }

    @Test
    public void editFoodNeedFail() {
        // Setup
        String name = "Burger";

        // Invoke
        boolean result = manager.editFoodNeed(name, 10, null, -1);

        // Analyze
        assertFalse(result);
    }

    @Test
    void getAllNeeds(){
        //Setup
        manager.addFoodNeed("Burger", 5, "Canned", 4);
        manager.addFoodNeed("Food", 6, "Fresh", 53);
        manager.addFoodNeed("Fish", 3, "Canned", 12);

        //Invoke
        Needs[] actual = manager.getAllNeeds();
        int len = actual.length;

        //Analyze
        assertEquals(3, len);
    }
}
