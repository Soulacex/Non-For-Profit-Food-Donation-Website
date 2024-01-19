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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.notifications.Notification;

public class NotificationFileDAOTest {
    NotificationFileDAO  notificationFileDAO;
    Notification[] testNotification;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNotificiationFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNotification = new Notification[4];
        testNotification[0] = new Notification(1, "helper1", "apple", 3);
        testNotification[1] = new Notification(2, "helper2", "steak", 1);
        testNotification[2] = new Notification(3, "helper3", "apple", 2);
        testNotification[3] = new Notification(4, "helper2", "borger", 5);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the account array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Notification[].class))
                .thenReturn(testNotification);
        notificationFileDAO = new NotificationFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetAllNotifications() {
        // Invoke
        Notification[] notification = notificationFileDAO.getAllNotifications();

        // Analyze
        assertEquals(notification.length,testNotification.length);
        for (int i = 0; i < testNotification.length;++i)
            assertEquals(notification[i],testNotification[i]);
    }

    @Test
    public void testGetNotification() {
        // Invoke
        Notification notification = notificationFileDAO.getNotification(1);

        // Analzye
        assertEquals(notification,testNotification[0]);
    }

    @Test
    public void testCreateNotification() {
        // Setup
        Notification notification = new Notification(5, "bob", "sushi", 1);
        // Invoke
        Notification result = assertDoesNotThrow(() -> notificationFileDAO.createNotification(notification),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Notification actual = notificationFileDAO.getNotification(notification.getId());
        assertEquals(actual.getId(),notification.getId());
        assertEquals(actual.toString(),notification.toString());
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Notification[].class));

        Notification notification = new Notification(5, "bob", "sushi", 1);

        assertThrows(IOException.class,
                        () -> notificationFileDAO.createNotification(notification),
                        "IOException not thrown");
    }

    @Test
    public void testGetNotificationNotFound() {
        // Invoke
        Notification notification = notificationFileDAO.getNotification(98);

        // Analyze
        assertEquals(notification,null);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the NotificationFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Notification[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new NotificationFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
