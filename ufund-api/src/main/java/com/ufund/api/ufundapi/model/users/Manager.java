package com.ufund.api.ufundapi.model.users;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

@Repository
/**
 * Displays the functionality of a manager
 * 
 * @author Kelvin Ng, Mason Zeng, Charles von Goins II, Sam Pepperman
 */
public class Manager extends Users{
    private static final Logger LOG = Logger.getLogger(Manager.class.getName());
    private Cupboard cupboard;

    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("type") private String type;
    @JsonProperty("quantity") private int quantity;

    /**
    * Creates a manager that is able to do actions to the cupboard

    *@param cupboard_object the cupboard that the manager has access to
    **/
    public Manager(Cupboard cupboardObject){
        super(cupboardObject);
        cupboard = cupboardObject;
    }

    /**
     * The manager creates a new Need in the Cupboard
     * @param name The name of need.
     * @param cost The cost of 1 of the need item.
     * @param type Type of need.
     * @param quantity Number of need items.
     * @return Returns new Need added.
     */
    public Needs addFoodNeed(@JsonProperty("name") String name, @JsonProperty("cost") double cost, 
    @JsonProperty("type") String type, @JsonProperty("quantity") Integer quantity) {
        Needs newNeed = cupboard.createNeed(name, cost, type, quantity);
        return newNeed;
    }

    /**
    * The manager removes a Need from the Cupboard
    * @param name The name of the Needs object that needs to be removed.
    * @return Returns true if the need is removed successfully, false otherwise.
    **/
    public boolean removeFoodNeed(String name) {
        boolean removed = cupboard.deleteNeeds(name);
        return removed;
    }

    /**
    * Updates the quantity of needs in the CupboardList according to its respective name in the list.
    *
    * @param name The name of the Need that is to be updated.
    * @param newCost The new cost of the Need that is being updated. 
    * @param newType The new type of the Need that is being updated. 
    * @param newQuantity The new quantity of the Need that is being updated. 
    *
    * @return Returns true if the need is edited successfully, false otherwise.
    **/
    public boolean editFoodNeed(String name, double newCost, String newType, Integer newQuantity) {
        boolean updatedNeed = cupboard.updateNeed(name, newCost, newType, newQuantity);
        return updatedNeed;
    }

    /**
    * Gets all the Needs from the cupboard and retuns it
    * @return ArrayList of all the Needs in the Cupboard
    **/
    public Needs[] getAllNeeds() {
        ArrayList<Needs> temp = cupboard.getCupboard();
        Needs[] cupboardNeeds = new Needs[temp.size()];
        return temp.toArray(cupboardNeeds);
    }
}
