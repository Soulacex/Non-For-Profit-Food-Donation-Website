package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class FundingBasketTests {
    private Cupboard cupboard;
    private FundingBasket fundingBasket;
    private Needs need_1;
    private Needs  need_2;
    private Needs need_3;

    @BeforeEach
    public void setUp() {
        cupboard = new Cupboard();
        fundingBasket = new FundingBasket(cupboard);
        
        //Setup
        cupboard.createNeed("Beans", 5, "Canned", 937);
        cupboard.createNeed("Carrots", 6, "Fresh", 53);
        cupboard.createNeed("Corn", 3, "Canned", 4);
        
        need_1 = cupboard.searchNameNeed("Beans");
        need_2 = cupboard.searchNameNeed("Carrots");
        need_3 = cupboard.searchNameNeed("Corn");
        
    }
    @Test
    public void getSupportedNeeds(){
        fundingBasket.addSupportedNeed("Beans", 37);
        fundingBasket.addSupportedNeed("Carrots", 3);
        fundingBasket.addSupportedNeed("Corn", 1);

        HashMap<String, Integer> test = fundingBasket.getSupportedNeeds();
        List<Needs> expected = Arrays.asList(need_1, need_2, need_3);
        
        assertEquals(test.size(), expected.size());
    }

    @Test
    public void getSupportedNeedsNone(){
        //Setup & Invoke
        int test = fundingBasket.getSupportedNeeds().size();
        int expected = 0; 
        
        //Analyze
        assertEquals(test, expected);
    }

    @Test
    public void addSupportedNeed(){
        //Invoke
        fundingBasket.addSupportedNeed("Beans", 37);
        fundingBasket.addSupportedNeed("Carrots", 3);
        fundingBasket.addSupportedNeed("Corn", 1);
        HashMap<String, Integer> test = fundingBasket.getSupportedNeeds();
        List<Needs> expected = new ArrayList<Needs>();
        expected.add(need_1);
        expected.add(need_2);
        expected.add(need_3);

        //Analyze
        assertEquals(test.size(), expected.size());
    }

    @Test
    public void addMultipleSupportedNeed(){
        //Invoke
        fundingBasket.addSupportedNeed("Beans", 37);
        fundingBasket.addSupportedNeed("Carrots", 3);
        fundingBasket.addSupportedNeed("Corn", 1);
        fundingBasket.addSupportedNeed("Beans", 1);
        HashMap<String, Integer> test = fundingBasket.getSupportedNeeds();
        List<Needs> expected = new ArrayList<Needs>();
        need_1.setQuantity(38);
        expected.add(need_1);
        expected.add(need_2);
        expected.add(need_3);
        
        //Analyze
        assertEquals(expected.get(0).getQuantity(), test.get(expected.get(0).getName()));
    }

    @Test
    public void addSupportedNeedNone(){
        //Invoke
        fundingBasket.addSupportedNeed("Soda", 24);
        int expected = 0;
        int test = fundingBasket.getSupportedNeeds().size();

        //Analyze
        assertEquals(test, expected);
    }

    @Test
    public void addSupportedNeedHighQuant(){
        //Invoke
        Needs test = fundingBasket.addSupportedNeed("Beans", 1000);

        //Analyze
        //Analyze
        assertEquals(test, null);
    }

    @Test
    public void removeSupportedNeed(){
        //Setup
        fundingBasket.addSupportedNeed("Beans", 37);
        fundingBasket.addSupportedNeed("Carrots", 3);
        fundingBasket.addSupportedNeed("Corn", 1);

        //Invoke
        fundingBasket.removeSupportedNeed("Beans");
        HashMap<String, Integer> test = fundingBasket.getSupportedNeeds();
        List<Needs> expected = new ArrayList<Needs>();
        expected.add(need_2);
        expected.add(need_3);

        //Tries to remove a Need that is not found in the Cupboard. Nothing should happen
        fundingBasket.removeSupportedNeed("Soda");
        
        //Analyze
        assertEquals(test.size(), expected.size());
    }

    @Test
    public void checkOut(){
        //Setup
        fundingBasket.addSupportedNeed("Beans", 37);
        fundingBasket.addSupportedNeed("Carrots", 3);
        fundingBasket.addSupportedNeed("Corn", 1);

        //Invoke
        //Test to see if needs quantity decreases when checked out
        fundingBasket.checkOut();
        int[] test_1 = new int[3];
        for(int i=0; i<cupboard.getCupboard().size(); i++){
            test_1[i] = cupboard.getCupboard().get(i).getQuantity();
        }
        //  test = {0,}
        int[] expected_1 = {900, 50, 3};

        //test to see that the funding basket gets reset after checked out
        int test_2 = fundingBasket.getSupportedNeeds().size();
        int expected_2 = 0;

        //Analyze
        assertArrayEquals(test_1, expected_1);
        assertEquals(test_2,expected_2);
    }

    @Test
    public void checkOutNone(){

        //Invoke
        fundingBasket.checkOut();
        int[] test = new int[3];
        for(int i=0; i<cupboard.getCupboard().size(); i++){
            test[i] = cupboard.getCupboard().get(i).getQuantity();
        }
        int[] expected = {937, 53, 4};

        //Analyze
        assertArrayEquals(test, expected);
    }
}