package com.ufund.api.ufundapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;

import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

public class FundingBasketControllerTest {
    private FundingBasketController FundingBasketController;
    private FundingBasketDAO mockFundingBasketDAO;

    /**
     * Before each test, generate a new FundingBasketController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockFundingBasketDAO = mock(FundingBasketDAO.class);
        FundingBasketController = new FundingBasketController(mockFundingBasketDAO);
    }

    @Test
    void getSupportedNeeds() throws IOException{
        // Setup
        HashMap<String, Integer> needs= new HashMap<>();
        needs.put("Beans", 10);
        needs.put("Burger", 7);
        needs.put("Ham", 13);
        
        when(mockFundingBasketDAO.getSupportedNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<HashMap<String ,Integer>> responseEntity = FundingBasketController.getSupportedNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(needs,responseEntity.getBody());
     }

    @Test
    public void getSupportedNeedsIOException() throws IOException {
        // Setup
        HashMap<String, Integer> needs= new HashMap<>();
        needs.put("Beans", 10);
        needs.put("Burger", 7);
        needs.put("Ham", 13);
        doThrow(new IOException()).when(mockFundingBasketDAO).getSupportedNeeds();

        // Invoke
        ResponseEntity<HashMap<String ,Integer>> responseEntity = FundingBasketController.getSupportedNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void addSupportedNeed() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
 
        when(mockFundingBasketDAO.addSupportedNeed(need.getName(), need.getQuantity())).thenReturn(need);

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.addSupportedNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void addSupportedNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Foo", 2, "Canned", 4);

        when(mockFundingBasketDAO.addSupportedNeed(need.getName(), need.getQuantity())).thenReturn(null);

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.addSupportedNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void addSupportedNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);

        doThrow(new IOException()).when(mockFundingBasketDAO).addSupportedNeed(need.getName(), need.getQuantity());

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.addSupportedNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }


    @Test
    //Delete needs does not return correctly
    public void removeSupportedNeed() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
 
        when(mockFundingBasketDAO.removeSupportedNeed(need.getName())).thenReturn(need);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.removeSupportedNeed("Food");

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void removeSupportedNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Frawp", 2, "Canned", 4);

        when(mockFundingBasketDAO.removeSupportedNeed(need.getName())).thenReturn(null);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.removeSupportedNeed("Frawp");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void removeSupportedNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);

        doThrow(new IOException()).when(mockFundingBasketDAO).removeSupportedNeed(need.getName());

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.removeSupportedNeed("Food");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void checkOut() throws IOException{
        // Setup
        when(mockFundingBasketDAO.checkOut()).thenReturn(true);

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.checkOut();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void checkOutFailure() throws IOException{
        // Setup
        when(mockFundingBasketDAO.checkOut()).thenReturn(false);

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.checkOut();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void checkOutIOException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockFundingBasketDAO).checkOut();

        // Invoke
        ResponseEntity<Needs> responseEntity = FundingBasketController.checkOut();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
