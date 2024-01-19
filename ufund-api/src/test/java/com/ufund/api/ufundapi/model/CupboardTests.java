package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CupboardTests {
    
    private Cupboard cupboard; 

    @BeforeEach
    public void setUp() {
        cupboard = new Cupboard();
    }

    @Test
    public void createNeedUniqueName() {
        // Setup
        Needs need = cupboard.createNeed("Food", 2, "Canned", 4);

        // Invoke
        String actual = need.getName();
        String expected = "Food";

        // Analyze
        assertEquals(actual, expected);
    }
    @Test
    public void createNeedNonUniqueName() {
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);

        // Invoke
        Needs actual = cupboard.createNeed("Food", 3, "Canned", 5);
        Object expected = null;

        // Analyze
        assertEquals(actual, expected);
    }

    @Test
    public void createNeedFailCost() {
        // Setup
        Needs expected = null;
        // Invoke
        Needs actual = cupboard.createNeed("Food", -1, "Canned", 4);

        // Analyze
        assertEquals(actual, expected);
    }

    @Test
    public void createNeedFailQuant() {
        // Setup
        Needs expected = null;
        // Invoke
        Needs actual = cupboard.createNeed("Food", 10, "Canned", 0);

        // Analyze
        assertEquals(actual, expected);
    }

    @Test
    public void updateExisitingNeed() {
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);

        // Invoke
        boolean result = cupboard.updateNeed("Food",3.5, "Packaged", 6);
        Needs updatedNeed = cupboard.getCupboard().get(0);

        // Analyze
        assertTrue(result);
        assertEquals("Food", updatedNeed.getName()); 
        assertEquals(3.5, updatedNeed.getCost(), 0.01);
        assertEquals("Packaged", updatedNeed.getType());
        assertEquals(6, updatedNeed.getQuantity());
    }

    @Test
    public void deleteExistingNeed() {
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);

        // Invoke
        cupboard.deleteNeeds("Food");

        // Analyze
        assertEquals(0, cupboard.getCupboard().size());
    }
    
    @Test
    void searchPartialName() {
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);
        cupboard.createNeed("Fo", 2, "Canned", 4);
        cupboard.createNeed("Fool", 2, "Canned", 4);
        cupboard.createNeed("Ham", 2, "Canned", 4);
        cupboard.createNeed("Slam", 2, "Canned", 4);

        // Invoke
        ArrayList<Needs> actual = cupboard.searchPartialName("o");
        String actualStrings = new String();
        for (int i = 0; i<actual.size();i++){
            actualStrings += actual.get(i).getName() + " ";
        }
        
        // Analyze
        Object expected = "Food Fo Fool ";
        assertEquals(actualStrings, expected);
    }
    
    @Test //this is Mason's code (DELETE LATER)
    void searchPartialNameNone(){
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);
        cupboard.createNeed("Fo", 2, "Canned", 4);
        cupboard.createNeed("Fool", 2, "Canned", 4);
        cupboard.createNeed("Ham", 2, "Canned", 4);
        cupboard.createNeed("Slam", 2, "Canned", 4);

        // Invoke
        ArrayList<Needs> actual = cupboard.searchPartialName("Beans");

        // Analyze
        int expected = 0;
        assertEquals(actual.size(), expected);
    }
  
  
    @Test
    void checkAllNeeds(){
        //Setup
        Cupboard cupboard = new Cupboard();
         cupboard.createNeed("Kelvin Ng knows his method", 5, "Canned", 4);
         cupboard.createNeed("Food", 6, "Fresh", 53);
         cupboard.createNeed("Charles von Goins II", 3, "Manpower", 4);

         //Invoke
         ArrayList<Needs> actual = cupboard.getCupboard();
         int len = actual.size();

         //Analyze
         assertEquals(3, len);
     }
    
    @Test //this is Mason's code (DELETE LATER)
    void checkAllNeedsContents(){
        //Setup
        Cupboard cupboard = new Cupboard();
         cupboard.createNeed("Beans", 5, "Canned", 4);
         cupboard.createNeed("Carrots", 6, "Fresh", 53);
         cupboard.createNeed("Corn", 3, "Canned", 4);

         //Invoke
         String[] test = new String[3];
         String[] actual = {"Beans", "Carrots", "Corn"};
         ArrayList<Needs> test_list = cupboard.getCupboard();
         for(int i=0; i<test_list.size();i++ ){
            test[i] = test_list.get(i).getName();
         }

         //Analyze
         assertArrayEquals(test, actual);
     }

    @Test
    public void getSingleNeed() {
        // Setup
        cupboard.createNeed("Food", 2, "Canned", 4);
        cupboard.createNeed("Fo", 2, "Canned", 4);
        cupboard.createNeed("Fool", 2, "Canned", 4);
        cupboard.createNeed("Goop", 2, "Canned", 4);

        // Invoke
        Needs actual = cupboard.searchNameNeed("Fool");

        // Analyze
        assertEquals("Fool", actual.getName());
        assertNotEquals("Foo",actual.getName());
        assertEquals(2, actual.getCost());
    }

    @Test //this is Mason's code (DELETE LATER)
    void deleteNeedsNone(){
         //Setup
         cupboard.createNeed("Beans", 5, "Canned", 4);
         cupboard.createNeed("Carrots", 6, "Fresh", 53);
         cupboard.createNeed("Corn", 3, "Canned", 4);

         //Invoke
         cupboard.deleteNeeds("Ham");
         int test = cupboard.getCupboard().size();
         int expected = 3;

         //Analyze
         assertEquals(test, expected);
    }

    @Test //this is Mason's code (DELETE LATER)
    void OrderforSearchPartial(){
         //Setup
         cupboard.createNeed("Potato", 5, "Fresh", 4);
         cupboard.createNeed("Tomato", 6, "Fresh", 53);
         cupboard.createNeed("Corn", 3, "Canned", 4);

         //Invoke
         ArrayList<Needs> test = cupboard.searchPartialName("To");
         ArrayList<Needs> expected = new ArrayList<Needs>();
         expected.add(cupboard.searchNameNeed("Tomato"));
         expected.add(cupboard.searchNameNeed("Potato"));

         //Analyze
         assertEquals(test, expected);
    }
    
}