package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Needs;


public class ManagerFileDAOTests {
    ManagerFileDAO ManagerFileDAO;
    Needs[] testNeeds;
    ObjectMapper mockObjectMapper;
    
     /**
     * Before each test, we will create and inject a Mock Object Mapper
     * @throws IOException
     */
    @BeforeEach
    public void setupManagerFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Needs[3];
        testNeeds[0] = new Needs("Beans",1,"Canned",10);
        testNeeds[1] = new Needs("Carrots",3,"Fresh",5);
        testNeeds[2] = new Needs("Apple Sauce",90,"Jarred",20);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
            .readValue(new File("test.txt"),Needs[].class))
                .thenReturn(testNeeds);
        ManagerFileDAO = new ManagerFileDAO("test.txt",mockObjectMapper);
    }

    @Test
    public void testGetAllNeeds() {
        // Invoke
        Needs[] needs = ManagerFileDAO.getAllNeeds();

        

        // Analyze
        assertEquals(needs.length,testNeeds.length);
        assertEquals(needs[1].getName(), testNeeds[0].getName());
        assertEquals(needs[2].getName(), testNeeds[1].getName());
        assertEquals(needs[0].getName(), testNeeds[2].getName());
    }


    @Test
    public void testRemoveFoodNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ManagerFileDAO.removeFoodNeed("Beans"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because Needs attribute of CupboardFileDAO is package private
        // we can access it directly
        assertEquals(ManagerFileDAO.Needs.size(),testNeeds.length-1);
    }

    @Test
    public void testAddFoodNeed() {
        // Setup
        Needs need = new Needs("Burger", 20, "Grilled", 5);

        // Invoke
        Needs result = assertDoesNotThrow(() -> ManagerFileDAO.addFoodNeed("Burger", 20, "Grilled", 5),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Needs actual = ManagerFileDAO.Needs.get(need.getName());
        assertEquals(actual.getName(),need.getName());
        assertEquals(actual.getCost(),need.getCost());
        assertEquals(actual.getType(),need.getType());
        assertEquals(actual.getQuantity(),need.getQuantity());
    }

    @Test
    public void testEditFoodNeedIO() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ManagerFileDAO.editFoodNeed("Beans", 20, "Grilled", 5),
                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,true);
    }

    @Test public void testEditFoodNeedNegativeCost(){
        //Invoke
        boolean result = assertDoesNotThrow(() ->ManagerFileDAO.editFoodNeed("Beans", -2, "Canned", 15));

        // Analyze
        assertEquals(result,true);
    }

    @Test public void testEditFoodNeedTypeNull(){
        //Invoke
        boolean result = assertDoesNotThrow(() ->ManagerFileDAO.editFoodNeed("Beans", 2, null, 15));

        // Analyze
        assertEquals(result,true);
    }

    @Test public void testEditFoodNeedNegativeQuantity(){
        //Invoke
        boolean result = assertDoesNotThrow(() ->ManagerFileDAO.editFoodNeed("Beans", 2, "Canned", -15));

        // Analyze
        assertEquals(result,true);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Needs[].class));

        assertThrows(IOException.class,
                        () -> ManagerFileDAO.addFoodNeed("Frog", 1, "Wild", 3),
                        "IOException not thrown");
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ManagerFileDAO.removeFoodNeed("Falafel"),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(ManagerFileDAO.Needs.size(),testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ManagerFileDAO.editFoodNeed("Falafel",1,"Fresh",12),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
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
                .readValue(new File("fail.txt"),Needs[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new CupboardFileDAO("fail.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}