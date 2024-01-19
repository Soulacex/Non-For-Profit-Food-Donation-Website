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
import com.ufund.api.ufundapi.model.HelperMessage.HelperMessage;
import com.ufund.api.ufundapi.persistence.HelperMessageDAO;

public class HelperMessageControllerTests {
    private HelperMessageController helperMessageController;
    private HelperMessageDAO mockHelperMessageDAO;

    /**
     * Before each test, generate a new AccountsController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockHelperMessageDAO = mock(HelperMessageDAO.class);
        helperMessageController = new HelperMessageController(mockHelperMessageDAO);
    }

    @Test
    public void createHelperMessageTest() throws IOException{
        // Setup
        HelperMessage message = new HelperMessage(1,"Sam","Hello");
        when(mockHelperMessageDAO.creatHelperMessage(message)).thenReturn(message);

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.createHelperMessage(message); 

        // Analyze
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }

    @Test
    public void createHelperMessageConflictTest() throws IOException{
    // Setup
        HelperMessage message = new HelperMessage(1,"Sam","Hello");
        when(mockHelperMessageDAO.creatHelperMessage(message)).thenReturn(null);

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.createHelperMessage(message); 

        // Analyze
        assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
        
    }

    @Test
    public void createHelperMessageIOExceptionTest() throws IOException{
        // Setup
        HelperMessage message = new HelperMessage(1,"Sam","Hello");
        doThrow(new IOException ()).when(mockHelperMessageDAO).creatHelperMessage(message);

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.createHelperMessage(message); 

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }

    @Test
    public void getAllMessagesTest() throws IOException{
        // Setup
        HelperMessage[] messages= new HelperMessage[2];
        messages[0] = new HelperMessage(0, "Brian", "Test1");
        messages[1] = new HelperMessage(1, "Smian", "Test2");
     
        when(mockHelperMessageDAO.getAllHelperMessages()).thenReturn(messages);

        // Invoke
        ResponseEntity<HelperMessage[]> responseEntity = helperMessageController.getAllMessages();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllMessagesIOExceptionTest() throws IOException {
        // Setup
        HelperMessage[] messages= new HelperMessage[2];
        messages[0] = new HelperMessage(0, "Brian", "Test1");
        messages[1] = new HelperMessage(1, "Smian", "Test2");
     
        doThrow(new IOException()).when(mockHelperMessageDAO).getAllHelperMessages();

        // Invoke
        ResponseEntity<HelperMessage[]> responseEntity = helperMessageController.getAllMessages();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getHelperMessageTest() throws IOException{
        // Setup
        HelperMessage message = new HelperMessage(0, "Sam", "Hello");

        when(mockHelperMessageDAO.searchMessage(message.getId())).thenReturn(message);

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.getHelperMessage(message.getId());
        
        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(message,responseEntity.getBody());
    }

    @Test
    public void getHelperMessageNotFoundTest() throws IOException{
        // Setup
        HelperMessage message = new HelperMessage(0, "Sam", "Hello");

        when(mockHelperMessageDAO.searchMessage(message.getId())).thenReturn(null);

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.getHelperMessage(message.getId());
        
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getHelperMessageIOExceptionTest() throws IOException {
        // Setup
        HelperMessage message = new HelperMessage(0, "Sam", "Hello");

        doThrow(new IOException()).when(mockHelperMessageDAO).searchMessage(message.getId());

        // Invoke
        ResponseEntity<HelperMessage> responseEntity = helperMessageController.getHelperMessage(message.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
