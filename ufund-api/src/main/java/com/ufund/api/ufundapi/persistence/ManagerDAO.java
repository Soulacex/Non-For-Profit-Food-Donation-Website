package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Needs;

public interface ManagerDAO {
    
    /**
     * The manager creates a new Need in the Cupboard
     * @param name The name of need.
     * @param cost The cost of 1 of the need item.
     * @param type Type of need.
     * @param quantity Number of need items.
     * @return Returns new Need added.
     */
    Needs addFoodNeed(String name, double cost, String type, Integer quantity) throws IOException;

    /**
    * The manager removes a Need from the Cupboard
    * @param name The name of the Needs object that needs to be removed.
    * @return Returns true if the need is removed successfully, false otherwise.
    **/
    boolean removeFoodNeed(String name) throws IOException;

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
    boolean editFoodNeed(String name, double cost, String type, Integer quantity) throws IOException;

    /**
    * Gets all the Needs from the cupboard and retuns it
    * @return ArrayList of all the Needs in the Cupboard
    **/
    Needs[] getAllNeeds() throws IOException;
}
