package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.persistence.CupboardDAO;

import java.util.ArrayList;

public class CupboardControllerTest {
    private CupboardController cupboardController;
    private CupboardDAO mockCupboardDAO;
    /**
     * Before each test, generate a new CupboardController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockCupboardDAO = mock(CupboardDAO.class);
        cupboardController = new CupboardController(mockCupboardDAO);
    }

    @Test
    public void createNeed() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
 
        when(mockCupboardDAO.createNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(need);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void createNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Foo", 2, "Canned", 4);
 
        when(mockCupboardDAO.createNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(null);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void createNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);

        doThrow(new IOException()).when(mockCupboardDAO).createNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity());

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void updateNeed() throws IOException{
         // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);

        when(mockCupboardDAO.updateNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(true);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void updateNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Foo", 2, "Canned", 4);
 
        when(mockCupboardDAO.updateNeed(need.getName(),need.getCost(),need.getType(),(int)need.getCost())).thenReturn(false);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.updateNeed(need);
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);
 
        doThrow(new IOException()).when(mockCupboardDAO).updateNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity());

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    //Delete needs does not return correctly
    public void deleteNeed() throws IOException{
        // Setup
        String needName = "Food";

        when(mockCupboardDAO.deleteNeeds(needName)).thenReturn(true);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.deleteNeed("Food");

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void deleteNeedFailure() throws IOException{
        // Setup
        String needName = "Frawp";

        when(mockCupboardDAO.deleteNeeds(needName)).thenReturn(false);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.deleteNeed("Frawp");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteNeedIOException() throws IOException {
        // Setup
        String needName = "Food";

        doThrow(new IOException()).when(mockCupboardDAO).deleteNeeds(needName);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.deleteNeed(needName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }


    @Test
    public void searchNeedName() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);

        when(mockCupboardDAO.searchNameNeed(need.getName())).thenReturn(need);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.searchNameNeed(need.getName());
        
        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void searchNeedNameNotFound() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);

        when(mockCupboardDAO.searchNameNeed(need.getName())).thenReturn(null);

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.searchNameNeed(need.getName());
        
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void searchNeedNameIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);

        doThrow(new IOException()).when(mockCupboardDAO).searchNameNeed(need.getName());

        // Invoke
        ResponseEntity<Needs> responseEntity = cupboardController.searchNameNeed(need.getName());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    
    
    @Test
    void searchPartialName() throws IOException{
        // Setup
        String testSearchString = "oo";
        ArrayList<Needs> needs= new ArrayList<>();
        needs.add(0, new Needs("Fo", 10, "Canned", 90));
        needs.add(1, new Needs("Fool", 8, "Canned", 31));
        needs.add(2, new Needs("Food", 1, "Canned", 0));

        when(mockCupboardDAO.searchPartialName(testSearchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<ArrayList<Needs>> responseEntity = cupboardController.searchPartialName(testSearchString);

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(needs,responseEntity.getBody());
    }

    @Test
    public void searchPartialNameIOException() throws IOException {
        // Setup
        String testSearchString = "oo";
        ArrayList<Needs> needs= new ArrayList<>();
        needs.add(0, new Needs("Fo", 10, "Canned", 90));
        needs.add(1, new Needs("Fool", 8, "Canned", 31));
        needs.add(2, new Needs("Food", 1, "Canned", 0));

        doThrow(new IOException()).when(mockCupboardDAO).searchPartialName(testSearchString);

        // Invoke
        ResponseEntity<ArrayList<Needs>> responseEntity = cupboardController.searchPartialName(testSearchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getAllNeeds() throws IOException{
        // Setup
        ArrayList<Needs> needs= new ArrayList<>();
        needs.add(0, new Needs("Fo", 10, "Canned", 90));
        needs.add(1, new Needs("Fool", 8, "Canned", 31));
        needs.add(2, new Needs("Food", 1, "Canned", 0));

        when(mockCupboardDAO.getCupboard()).thenReturn(needs);

        // Invoke
        ResponseEntity<ArrayList<Needs>> responseEntity = cupboardController.getAllNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(needs,responseEntity.getBody());
     }

    @Test
    public void getAllNeedsIOException() throws IOException {
        // Setup
        ArrayList<Needs> needs= new ArrayList<>();
        needs.add(0, new Needs("Fo", 10, "Canned", 90));
        needs.add(1, new Needs("Fool", 8, "Canned", 31));
        needs.add(2, new Needs("Food", 1, "Canned", 0));

        doThrow(new IOException()).when(mockCupboardDAO).getCupboard();

        // Invoke
        ResponseEntity<ArrayList<Needs>> responseEntity = cupboardController.getAllNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}