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
import java.util.HashMap;
import java.util.Map;

import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.model.SupportedNeed;
import com.ufund.api.ufundapi.persistence.HelperDAO;

public class HelperControllerTest{
    private HelperController helperController;
    private HelperDAO mockHelperDAO;

    @BeforeEach
    public void setUp() {
        mockHelperDAO = mock(HelperDAO.class);
        helperController = new HelperController(mockHelperDAO);
    }

    @Test
    public void addSupportedNeed() throws IOException{
          SupportedNeed supportNeed = new SupportedNeed("Joe", "Frog", 63);
          Needs need = new Needs("Frog", 25.2, "Fresh", 23);

        when(mockHelperDAO.addSupportedNeed(supportNeed)).thenReturn(need);
    // Invoke
        ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(need,responseEntity.getBody());
    }

    @Test
    public void addSupportedNeedFailure() throws IOException{
        // Setup
        SupportedNeed supportNeed = new SupportedNeed("Joe", "Boy", 63);

        when(mockHelperDAO.addSupportedNeed(supportNeed)).thenReturn(null);

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void addSupportedNeedIOException() throws IOException {
        // Setup
        SupportedNeed supportNeed = new SupportedNeed("Joe", "Boy", 63);
        
        doThrow(new IOException()).when(mockHelperDAO).addSupportedNeed(supportNeed);

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.addSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    //Delete needs does not return correctly
    public void removeSupportedNeed() throws IOException{
        // Setup
        Needs need = new Needs("Food", 2, "Canned", 4);
        SupportedNeed supportNeed = new SupportedNeed("Joe", "Boy", 63);
        when(mockHelperDAO.removeSupportedNeed(supportNeed)).thenReturn(need);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void removeSupportedNeedFailure() throws IOException{
        // Setup
        SupportedNeed supportNeed = new SupportedNeed("Joe", "Boy", 63);
        
        when(mockHelperDAO.removeSupportedNeed(supportNeed)).thenReturn(null);
        
        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void removeSupportedNeedIOException() throws IOException {
        // Setup
        SupportedNeed supportNeed = new SupportedNeed("Joe", "Boy", 63);

        doThrow(new IOException()).when(mockHelperDAO).removeSupportedNeed(supportNeed);

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.removeSupportedNeed(supportNeed);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void checkOut() throws IOException{
        // Setup
        when(mockHelperDAO.checkOut("bob")).thenReturn(true);

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.checkOut("bob");

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void checkOutFailure() throws IOException{
        // Setup
        when(mockHelperDAO.checkOut("bob")).thenReturn(false);

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.checkOut("bob");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void checkOutIOException() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockHelperDAO).checkOut("bob");

        // Invoke
        ResponseEntity<Needs> responseEntity = helperController.checkOut("bob");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getSupportedNeeds() throws IOException{
        Map<String, SupportedNeed> list_Needs = new HashMap<>();
        list_Needs.put("helper1", new SupportedNeed("helper1", "Fish", 23));
        list_Needs.put("helper1", new SupportedNeed("helper1", "Frog", 54));
        list_Needs.put("helper1", new SupportedNeed("helper1", "Boy", 93));

        when(mockHelperDAO.getSupportedNeeds("helper1")).thenReturn(list_Needs);

        // Invoke
        ResponseEntity<Map<String, SupportedNeed>> responseEntity = helperController.getSupportedNeeds("helper1");

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(list_Needs,responseEntity.getBody());
     }

     @Test
    public void getSupportedNeedsIOException() throws IOException{
        Map<String, SupportedNeed> list_Needs = new HashMap<>();
        list_Needs.put("helper1", new SupportedNeed("helper1", "Fish", 23));
        list_Needs.put("helper1", new SupportedNeed("helper1", "Frog", 54));
        list_Needs.put("helper1", new SupportedNeed("helper1", "Boy", 93));

        doThrow(new IOException()).when(mockHelperDAO).getSupportedNeeds("helper1");

        // Invoke
        ResponseEntity<Map<String, SupportedNeed>> responseEntity = helperController.getSupportedNeeds("helper1");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
     }
}
    


