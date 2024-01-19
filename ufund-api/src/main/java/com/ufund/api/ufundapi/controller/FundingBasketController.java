package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

  /**
 * Handles the REST API requests for the FundingBasket resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/fundingbasket")
public class FundingBasketController{
    private static final Logger LOG = Logger.getLogger(FundingBasketController.class.getName());
    private FundingBasketDAO fbDao;

/**
     * Creates a REST API controller to reponds to requests
     * 
     * @param fbDao The {@link fbDAO Account Data Access Object} to perform CRUD operations
     * 
     * This dependency is injected by the Spring Framework
     */
    public FundingBasketController(FundingBasketDAO fbDao) {
        this.fbDao = fbDao;
    }

    /**
     * Responds to the GET request for all {@linkplain Needs need}
     * 
     * @return ResponseEntity with List of {@link Needs need} objects and HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<HashMap<String, Integer>> getSupportedNeeds() {
        LOG.info("GET /fundingbasket");
        try{
            HashMap<String, Integer> supportedNeeds = new HashMap<>();
            supportedNeeds = fbDao.getSupportedNeeds();
            return new ResponseEntity<HashMap<String, Integer>>(supportedNeeds,HttpStatus.OK);
        }
        catch(IOException IOE) {
            LOG.log(Level.SEVERE, IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a {@linkplain Needs need} with the provided account object
     * 
     * @param need - The {@link Needs need} to add
     * @param quant - The {@link Needs quant} of that need to add
     * 
     * @return ResponseEntity with added {@link Needs need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Needs need} object is not in Cupboard
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/addneed")
    public ResponseEntity<Needs> addSupportedNeed(@RequestBody Needs need) {
        LOG.info("POST /fundingbasket/addneed");
        try {
            String name = need.getName();
            Integer quantity = need.getQuantity();
            Needs addedNeed = fbDao.addSupportedNeed(name, quantity);
            
            if (addedNeed != null) {
                addedNeed.setCost(need.getCost()); 
                addedNeed.setType(need.getType()); 
                return new ResponseEntity<>(addedNeed, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE, IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Needs need} with the given need name
     * 
     * @param name The name of the {@link Needs need} to be deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Needs> removeSupportedNeed(@PathVariable String name) {
        LOG.info("DELETE /fundingbasket/" + name);
        try {
            Needs removedNeed = fbDao.removeSupportedNeed(name);
            if (removedNeed != null) {
                return new ResponseEntity<>(removedNeed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
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
    @PutMapping("/checkout")
    public ResponseEntity<Needs> checkOut() {
        LOG.info("PUT /fundingbasket/checkout");
        try {
            boolean success = fbDao.checkOut();
            if (success!=false)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}