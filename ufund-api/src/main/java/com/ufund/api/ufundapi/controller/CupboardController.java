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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.CupboardDAO;
import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;


/**
 * Handles the REST API requests for the Needs resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */

@RestController
@RequestMapping("/ufund")
public class CupboardController {
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
    private CupboardDAO cupboardDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param cupboardDao The {@link CupboardDAO Cupboard Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CupboardController(CupboardDAO cupboardDao) {
        this.cupboardDao = cupboardDao;
    }

    /**
     * Creates a {@linkplain Needs need} with the provided need object
     * 
     * @param need - The {@link Needs need} to create
     * 
     * @return ResponseEntity with created {@link Needs need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Needs need} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Needs> createNeed(@RequestBody Needs need) {
        LOG.info("POST /ufund/createNeed");

        try {
            Needs newNeed = cupboardDao.createNeed(need.getName(), need.getCost(), need.getType(), need.getQuantity());
            if (newNeed != null)
                return new ResponseEntity<Needs>(newNeed, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException IOE) {
            LOG.log(Level.SEVERE,IOE.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Needs need} with the provided {@linkplain Needs updatedNeed} object, if it exists 
     *     
     * @param need The {@link Needs need} to update.
     * 
     * @return ResponseEntity with updated {@link Needs need} object and HTTP status of OK if updated<br>
     * ResponseEntity with an empty list and HTTP status of OK if the list if empty.
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Needs> updateNeed(@RequestBody Needs need) {
        LOG.info("PUT /ufund " + need);
        try {
            boolean success = cupboardDao.updateNeed(
                need.getName(),
                need.getCost(),
                need.getType(),
                need.getQuantity()
            );
            if (success!=false)
                return new ResponseEntity<>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Needs need} with the given id
     * 
     * @param name The name of the {@link Needs need} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Needs> deleteNeed(@PathVariable String name) {
        LOG.info("DELETE /ufund/" + name);
        try {
            boolean deletedNeed = cupboardDao.deleteNeeds(name);
            if(deletedNeed != false){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Cupboard cupboard} for the given name
     * 
     * @param name The unique name given to the {@link Needs need}
     * 
     * @return ResponseEntity with {@link Needs need} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{name}")
    public ResponseEntity<Needs> searchNameNeed(@PathVariable String name) {
        LOG.info("GET /Cupboard/" + name);
        try {
            Needs foundNeed = cupboardDao.searchNameNeed(name);
            if (foundNeed != null)
                return new ResponseEntity<Needs>(foundNeed,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/**
     * Responds to the GET request for all {@linkplain Needs need} whose name contains
     * the text in name
     * 
     * @param name The string of the name which contains the text used to find the {@link Needs need}
     * 
     * @return ResponseEntity with array list of {@link Needs need} objects (may be empty) and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/")
    public ResponseEntity<ArrayList<Needs>> searchPartialName(@RequestParam String name) {
        LOG.info("GET /Cupboard/?name = " + name);

        try {
            ArrayList<Needs> foundNeeds = cupboardDao.searchPartialName(name);
            return new ResponseEntity<ArrayList<Needs>>(foundNeeds,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
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
    @GetMapping("")
    public ResponseEntity<ArrayList<Needs>> getAllNeeds() {
        LOG.info("GET /needs");
        try{
            ArrayList<Needs> list_Needs = new ArrayList<>();
            list_Needs = cupboardDao.getCupboard();
                return new ResponseEntity<ArrayList<Needs>>(list_Needs,HttpStatus.OK);
            }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}