package com.ufund.api.ufundapi.controller;
import java.io.IOException;
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

import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.persistence.ManagerDAO;

/**
 * Handles the REST API requests for the Manager resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author FundsInHighPlaces
 */
@RestController
@RequestMapping("/admin")
public class ManagerController {
    private static final Logger LOG = Logger.getLogger(ManagerController.class.getName());
    private ManagerDAO managerDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param managerDAO The {@link ManagerDAO Manager Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public ManagerController(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    /**
     * Responds to the GET request for all {@linkplain Needs need}
     * 
     * @return ResponseEntity with ArrayList of {@link Needs need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Needs[]> getAllNeeds() {
        LOG.info("GET /manager/needs");
        try{
            Needs[] list_Needs = managerDAO.getAllNeeds();
                return new ResponseEntity<Needs[]>(list_Needs,HttpStatus.OK);
            }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Needs> addFoodNeed(@RequestBody Needs need) {
        LOG.info("POST /manager/createFoodNeed");

        try {
            Needs newNeed = managerDAO.addFoodNeed(need.getName(), need.getCost(), need.getType(), need.getQuantity());
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
     * Deletes a {@linkplain Needs need} with the given id
     * 
     * @param name The name of the {@link Needs need} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Needs> removeFoodNeed(@PathVariable String name) {
        LOG.info("DELETE /manager/" + name);
        try {
            boolean deletedNeed = managerDAO.removeFoodNeed(name);
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
    public ResponseEntity<Needs> editFoodNeed(@RequestBody Needs need) {
        LOG.info("PUT /manager " + need);
        try {
            boolean success = managerDAO.editFoodNeed(
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
}
