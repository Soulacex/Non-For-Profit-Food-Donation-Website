package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.accounts.Accounts;
import com.ufund.api.ufundapi.model.accounts.HelperAccount;
import com.ufund.api.ufundapi.model.notifications.Notification;
import com.ufund.api.ufundapi.persistence.NotificationDAO;

/**
 * Handles the REST API requests for the Notification resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private static final Logger LOG = Logger.getLogger(NotificationController.class.getName());
    private NotificationDAO notificationDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param notificationDAO The {@link notificationDAO Notification Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public NotificationController(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    /**
     * Creates a {@linkplain Notification notification} with the provided inputs
     * 
     * @param notification Notification to create
     * 
     * @return ResponseEntity with created {@link Notification notification} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Notification notification} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        LOG.info("POST /notification/createNotification");
        try {
            Notification newNotification = notificationDAO.createNotification(notification);
            return new ResponseEntity<Notification>(newNotification, HttpStatus.CREATED);
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE,IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Notification notification} for the given username
     * 
     * @param Id The unique Id given to the {@link Notification notification}
     * 
     * @return ResponseEntity with {@link Notification notification} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{Id}")
    public ResponseEntity<Notification> getNotification(@PathVariable int Id) {
        LOG.info("GET /notification/" + Id);
        try {
            Notification notification = notificationDAO.getNotification(Id);
            if (notification != null)
                return new ResponseEntity<Notification>(notification,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Notification notification}
     * 
     * @return ResponseEntity with ArrayList of {@link Notification notification} objects and HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Notification[]> getAllNotifications() {
        LOG.info("GET /notification");
        try{
            Notification[] allAccounts = notificationDAO.getAllNotifications();
            return new ResponseEntity<Notification[]>(allAccounts,HttpStatus.OK);
        }
        catch(IOException IOE) {
            LOG.log(Level.SEVERE, IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
