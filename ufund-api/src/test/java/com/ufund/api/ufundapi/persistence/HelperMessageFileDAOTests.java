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

import com.ufund.api.ufundapi.model.HelperMessage.*;

public class HelperMessageFileDAOTests {
    HelperMessageFileDAO helperMessageFileDAO;
    HelperMessage[] testMessages;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupAccountsFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testMessages = new HelperMessage[4];
        testMessages[0] = new HelperMessage(0, "Test1", "Test1");
        testMessages[1] = new HelperMessage(1, "Test2", "Test2");
        testMessages[2] = new HelperMessage(2, "Test3", "Test3");
        testMessages[3] = new HelperMessage(3, "Test4", "Test4");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the account array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),HelperMessage[].class))
                .thenReturn(testMessages);
        helperMessageFileDAO = new HelperMessageFileDAO("doesnt_matter.txt", mockObjectMapper);
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
                .readValue(new File("doesnt_matter.txt"),HelperMessage[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new HelperMessageFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(HelperMessage[].class));

        HelperMessage message = new HelperMessage(99, "ppqpqp", "pqwpepq");

        assertThrows(IOException.class,
                        () -> helperMessageFileDAO.creatHelperMessage(message),
                        "IOException not thrown");
    }

    @Test
    public void testCreateHelperMessage(){
        //Setup
        HelperMessage message = new HelperMessage(0, "Sam", "Test1");
        // Invoke
        HelperMessage result = assertDoesNotThrow(() -> helperMessageFileDAO.creatHelperMessage(message),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        String actual = message.getMessage();
        assertEquals(actual,result.getMessage());


    }

    @Test
    public void testSearchMessage(){
        // Invoke
        HelperMessage message = helperMessageFileDAO.searchMessage(0);

        // Analyze
        assertEquals(message, testMessages[0]);
    }

    @Test
    public void testSearchMessageFail(){
        //Invoke
        HelperMessage message = helperMessageFileDAO.searchMessage(4);

        //Analyze
        assertEquals(message, null);
    }

    @Test
    public void testGetAllHelperMessages(){
        // Invoke
        HelperMessage[] messages = helperMessageFileDAO.getAllHelperMessages();

        // Analyze
        assertEquals(messages.length,testMessages.length);
        for (int i = 0; i < testMessages.length;++i)
            assertEquals(messages[i],testMessages[i]);
    }
}