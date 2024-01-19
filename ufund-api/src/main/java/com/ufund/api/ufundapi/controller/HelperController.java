package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.model.SupportedNeed;
import com.ufund.api.ufundapi.persistence.HelperDAO;

/**
 * Handles the REST API requests for the Account resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/helper")
public class HelperController{  
    private static final Logger LOG = Logger.getLogger(HelperController.class.getName());
    private HelperDAO helperDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param helperDao The {@link accountDAO Account Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public HelperController(HelperDAO helperDao) {
        this.helperDao = helperDao;
    }

    /**
     * Adds a {@linkplain Needs need} with the provided account object
     * 
     * @param need - The {@link Needs need} to add
     * 
     * @return ResponseEntity with added {@link Needs need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Needs need} object is not in Cupboard
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/addneed")
    public ResponseEntity<Needs> addSupportedNeed(@RequestBody SupportedNeed supportedNeed) {
        LOG.info("POST /helper/fundingbasket/addneed"+supportedNeed.getSupportedNeedName());
        try {

            Needs addedNeed = helperDao.addSupportedNeed(supportedNeed);

            if (addedNeed != null)
                return new ResponseEntity<Needs>(addedNeed, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE, IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Needs need} with the given need name
     * 
     * @param need_name The name of the {@link Needs need} to be deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    //Should this be @pathvariable ???
    @DeleteMapping("/")
    public ResponseEntity<Needs> removeSupportedNeed(@RequestBody SupportedNeed supportedNeed) {
        String need_name = supportedNeed.getSupportedNeedName();
        LOG.info("DELETE /helper/fundingbasket/" + need_name);
         try {
            if(helperDao.removeSupportedNeed(supportedNeed) != null){
                return new ResponseEntity<Needs>(HttpStatus.OK);
            }
        else{
            return new ResponseEntity<Needs>(HttpStatus.NOT_FOUND);
        }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Cupboard cupboard} after checking out {@linkplain Needs need}
     *    
     * @return HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if unable to checkout<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}")
    public ResponseEntity<Needs> checkOut(@PathVariable String username) {
        LOG.info("PUT /helper/checkout/"+username);
        try {
            //String username = supportedNeed.getSupportedNeedName();
            boolean success = helperDao.checkOut(username);
            if (success!=false)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /**
     * Responds to the GET request for all {@linkplain Needs need}
     * 
     * @return ResponseEntity with ArrayList of {@link Needs need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, SupportedNeed>> getSupportedNeeds(@PathVariable String username) {
        LOG.info("GET /helper/needs/"+username);
        try{
            Map<String, SupportedNeed> list_Needs = helperDao.getSupportedNeeds(username);
                return new ResponseEntity<Map<String, SupportedNeed>>(list_Needs,HttpStatus.OK);
            }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
