package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.ArrayList;


import com.ufund.api.ufundapi.model.Needs;

public interface CupboardDAO {
/**
     * Checks if needs name is unique then creates a new Need based on parameters and adds it to the CupboardList.
     * @param name The name of need.
     * @param cost The cost of 1 of the need item.
     * @param type Type of need.
     * @param quantity Number of need items.
     * @return Returns new Need created.
     */
    Needs createNeed(String name, double cost, String type, Integer quantity) throws IOException;

    /**
    * Updates the quantity of needs in the CupboardList according to its respective name in the list.
    *
    * @param currentName The current name of the Need that is being updated.
    * @param newName The new name of the Need that is to be updated.
    * @param newCost The new cost of the Need that is being updated. 
    * @param newQuantity The new quantity of the Need that is being updated. 
    * @param newType The new type of the Need that is being updated. 
    *
    * @return Return true if the need is successfully, false otherwise.
    **/
    boolean updateNeed(String name, double newCost, String newType, Integer newQuantity) throws IOException;

    /**
    * Deletes a need from the Cupboard List based on its name. 
    *
    * @param name The name of the Needs object that needs to be deleted.
    *
    **/
    boolean deleteNeeds(String name) throws IOException;
    
    /**
    * Since each need has a unique name, searching for a name in the array
    * will return the specific need requested
    * @param String if the name being searched
    *
    * @return Needs: the need that has that specific name
    *         Null if no Need with the name exists
    **/
    Needs searchNameNeed(String needName) throws IOException;

    /**
    * Takes in a string and returns all needs containing the string
    *
    * @param String searchText the text to find in the need names
    *
    * @return ArrayList<needs>: the list of all needs containing the search string
    *         if search string is null all needs should be returned
    **/
    ArrayList<Needs> searchPartialName(String searchText) throws IOException;

    /**
    * Function that returns all the Needs within the Cupboard
    * @return ArrayList of all the Needs within the Cupboard
    **/
    ArrayList<Needs> getCupboard() throws IOException;
}