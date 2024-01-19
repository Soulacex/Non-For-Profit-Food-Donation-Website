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
import com.ufund.api.ufundapi.persistence.ManagerDAO;

public class ManagerControllerTest {
    private ManagerController managerController;
    private ManagerDAO mockManagerDAO;
    /**
     * Before each test, generate a new ManagerController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockManagerDAO = mock(ManagerDAO.class);
        managerController = new ManagerController(mockManagerDAO);
    }

    @Test
    void getAllNeeds() throws IOException{
        // Setup
        Needs[] needs= new Needs[3];
        needs[0] = new Needs("Fo", 10, "Canned", 90);
        needs[1] = new Needs("Fool", 8, "Canned", 31);
        needs[2] = new Needs("Food", 1, "Canned", 0);

        when(mockManagerDAO.getAllNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Needs[]> responseEntity = managerController.getAllNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(needs,responseEntity.getBody());
     }

    @Test
    public void getAllNeedsIOException() throws IOException {
        // Setup
        Needs[] needs= new Needs[3];
        needs[0] = new Needs("Fo", 10, "Canned", 90);
        needs[1] = new Needs("Fool", 8, "Canned", 31);
        needs[2] = new Needs("Food", 1, "Canned", 0);
        
        doThrow(new IOException()).when(mockManagerDAO).getAllNeeds();

        // Invoke
        ResponseEntity<Needs[]> responseEntity = managerController.getAllNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void addFoodNeed() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
        
        when(mockManagerDAO.addFoodNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(need);

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.addFoodNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void addFoodNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Foo", 2, "Canned", 4);
        
        when(mockManagerDAO.addFoodNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(null);

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.addFoodNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void addFoodNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);
      
        doThrow(new IOException()).when(mockManagerDAO).addFoodNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity());

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.addFoodNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void editFoodNeed() throws IOException{
         // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
       
        when(mockManagerDAO.editFoodNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity())).thenReturn(true);

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.editFoodNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void updateNeedFailure() throws IOException{
        // Setup
        Needs need = new Needs("Foo", 2, "Canned", 4);
        
        when(mockManagerDAO.editFoodNeed(need.getName(),need.getCost(),need.getType(),(int)need.getCost())).thenReturn(false);

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.editFoodNeed(need);
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateNeedIOException() throws IOException {
        // Setup
        Needs need = new Needs("Fo", 10, "Canned", 90);

        doThrow(new IOException()).when(mockManagerDAO).editFoodNeed(need.getName(),need.getCost(),need.getType(),need.getQuantity());

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.editFoodNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    //Delete needs does not return correctly
    public void removeFoodNeed() throws IOException{
        // Setup
        String needName = "Food";
        
        when(mockManagerDAO.removeFoodNeed(needName)).thenReturn(true);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.removeFoodNeed("Food");

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void deleteNeedFailure() throws IOException{
        // Setup
        String needName = "Frawp";
        
        when(mockManagerDAO.removeFoodNeed(needName)).thenReturn(false);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.removeFoodNeed("Frawp");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteNeedIOException() throws IOException {
        // Setup
        String needName = "Food";
        
        doThrow(new IOException()).when(mockManagerDAO).removeFoodNeed(needName);

        // Invoke
        ResponseEntity<Needs> responseEntity = managerController.removeFoodNeed(needName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}