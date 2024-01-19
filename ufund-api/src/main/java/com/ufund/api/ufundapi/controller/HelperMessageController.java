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
import com.ufund.api.ufundapi.model.HelperMessage.*;
import com.ufund.api.ufundapi.model.accounts.HelperAccount;
import com.ufund.api.ufundapi.persistence.HelperMessageDAO;


/**
 * Handles the REST API requests for the HelperMessage resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/message")
public class HelperMessageController {
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
    private HelperMessageDAO helperMessageDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param helperMessageDao The {@link helperMessageDAO helper message Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public HelperMessageController(HelperMessageDAO helperMessageDAO) {
        this.helperMessageDAO = helperMessageDAO;
    }

    /**
     * Creates a {@linkplain helperMessage helpermessage} with the provided inputs
     * 
     * @param username - The {@link hel username} to create
     * @param message - The {@link helperMessage message} to create
     * 
     * @return ResponseEntity with created {@link HelperAccount account} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link HelperAccount account} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<HelperMessage> createHelperMessage(@RequestBody HelperMessage helperMessage) {
        LOG.info("POST /helperMessage/createMessage");
        try {
            HelperMessage newMessage = helperMessageDAO.creatHelperMessage(helperMessage);
            if (newMessage != null)
                return new ResponseEntity<HelperMessage>(newMessage, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE,IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain HelperMessage helperMessage} for the given username
     * 
     * @param Id The unique Id given to the {@link HelperMessage message}
     * 
     * @return ResponseEntity with {@link HelperMessage message} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{Id}")
    public ResponseEntity<HelperMessage> getHelperMessage(@PathVariable int Id) {
        LOG.info("GET /helperMessage/" + Id);
        try {
            HelperMessage foundMessage = helperMessageDAO.searchMessage(Id);
            if (foundMessage != null)
                return new ResponseEntity<HelperMessage>(foundMessage,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain HelperMessage helperMessage} for the given username
     * 
     * @param Id The unique Id given to the {@link HelperMessage message}
     * 
     * @return ResponseEntity with {@link HelperMessage message} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<HelperMessage[]> getAllMessages() {
        LOG.info("GET /helperMessage/");
        try {
            HelperMessage[] foundMessage = helperMessageDAO.getAllHelperMessages();
            return new ResponseEntity<HelperMessage[]>(foundMessage,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
