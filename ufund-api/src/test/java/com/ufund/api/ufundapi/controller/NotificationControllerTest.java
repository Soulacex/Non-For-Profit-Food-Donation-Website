package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.notifications.Notification;
import com.ufund.api.ufundapi.persistence.NotificationDAO;

public class NotificationControllerTest {
    private NotificationController notificationController;
    private NotificationDAO mockNotificationDAO;

    /**
     * Before each test, generate a new NotificationController object and inject a mock DAO
     */
    @BeforeEach
    public void setUp() {
        mockNotificationDAO = mock(NotificationDAO.class);
        notificationController = new NotificationController(mockNotificationDAO);
    }

    @Test
    public void createNotification() throws IOException{
        // Setup
        Notification notification = new Notification(1, "helper1", "apple", 3);
        when(mockNotificationDAO.createNotification(notification)).thenReturn(notification);

        // Invoke
        ResponseEntity<Notification> responseEntity = notificationController.createNotification(notification); 

        // Analyze
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(notification, responseEntity.getBody());
    }

    @Test
    public void createNotificationIOExcpetion() throws IOException{
        // Setup
        Notification notification = new Notification(1, "helper1", "apple", 3);
        doThrow(new IOException ()).when(mockNotificationDAO).createNotification(notification);

        // Invoke
        ResponseEntity<Notification> responseEntity = notificationController.createNotification(notification); 

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }

    @Test
    public void getAllNotifications() throws IOException{
        // Setup
        Notification[] notifications = new Notification[2];
        notifications[0] = new Notification(1, "helper1", "apple", 3);
        notifications[1] = new Notification(2, "bob", "borger", 2);
     
        when(mockNotificationDAO.getAllNotifications()).thenReturn(notifications);

        // Invoke
        ResponseEntity<Notification[]> responseEntity = notificationController.getAllNotifications();

        // Analyze
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllNotificationsIOException() throws IOException {
        // Setup
        Notification[] notifications = new Notification[2];
        notifications[0] = new Notification(1, "helper1", "apple", 3);
        notifications[1] = new Notification(2, "bob", "borger", 2);
     
        doThrow(new IOException()).when(mockNotificationDAO).getAllNotifications();

        // Invoke
        ResponseEntity<Notification[]> responseEntity = notificationController.getAllNotifications();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNotification() throws IOException { // findHeroes may throw IOException
        // Setup
        int id = 1;
        Notification notification = new Notification(1, "helper1", "apple", 3);
        // When getNotification is called with the search int, return the 1
        /// notification above
        when(mockNotificationDAO.getNotification(1)).thenReturn(notification);

        // Invoke
        ResponseEntity<Notification> response = notificationController.getNotification(id);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(notification,response.getBody());
    }

    @Test
    public void testGetNotificationHandleException() throws IOException { // findHeroes may throw IOException
        // Setup
        int id = 10;
        // When getNotification is called on the Mock Notification DAO, throw an IOException
        doThrow(new IOException()).when(mockNotificationDAO).getNotification(id);

        // Invoke
        ResponseEntity<Notification> response = notificationController.getNotification(id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNotificationNotFound() throws IOException { // findHeroes may throw IOException
        // Setup
        int id = 10;
        // When getNotification is called on the Notification Hero DAO, return null
        when(mockNotificationDAO.getNotification(id)).thenReturn(null);
        // Invoke
        ResponseEntity<Notification> response = notificationController.getNotification(id);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}
