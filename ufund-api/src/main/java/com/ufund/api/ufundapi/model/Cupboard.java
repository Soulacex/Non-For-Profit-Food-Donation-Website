package com.ufund.api.ufundapi.model;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.stereotype.Repository;

@Repository
/**
 * Represents the Cupboard that contains all the needs for the food bank
 * 
 * @author Kelvin Ng, Mason Zeng, Charles von Goins II, Sam Pepperman
 */
public class Cupboard {
    private static final Logger LOG = Logger.getLogger(Cupboard.class.getName());
    private ArrayList<Needs> CupboardList;

    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("type") private String type;
    @JsonProperty("quantity") private int quantity;

    /**
    * Creates a Cupboard.
    *
    **/
    public Cupboard() {
        CupboardList = new ArrayList<Needs>();
    }

    /**
     * Checks if needs name is unique then creates a new Need based on parameters and adds it to the CupboardList.
     * @param name The name of need.
     * @param cost The cost of 1 of the need item.
     * @param type Type of need.
     * @param quantity Number of need items.
     * @return Returns new Need created.
     */
    public Needs createNeed(@JsonProperty("name") String name, @JsonProperty("cost") double cost, 
    @JsonProperty("type") String type, @JsonProperty("quantity") Integer quantity) {
        if(cost <= 0){
            return null;
        }

        if(quantity <= 0){
            return null;
        }
        
        for (Needs needs : CupboardList) {
            if(needs.getName().equals(name)) {
                return null;
            }
        }
        Needs need = new Needs(name, cost, type, quantity);
        CupboardList.add(need);
        return need;
    }

    /**
    * Deletes a need from the Cupboard List based on its name. 
    *
    * @param name The name of the Needs object that needs to be deleted.
    * @return Returns true if the need is deleted successfully, false otherwise.
    **/
    public boolean deleteNeeds(String name) {
        for (int i = 0; i < CupboardList.size(); i++) {
            Needs need = CupboardList.get(i);
            if (need.getName().equals(name)) {
                CupboardList.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
    * Since each need has a unique name, searching for a name in the array
    * will return the specific need requested
    * @param needName if the name being searched
    *
    * @return Needs: the need that has that specific name
    *         Null if no Need with the name exists
    **/
    public Needs searchNameNeed(String needName) {
        for(int i=0;i<CupboardList.size();i++){
            if(CupboardList.get(i).getName().equals(needName)){
                return CupboardList.get(i);
            }
        }
        return null;
    }

    /**
    * Takes in a string and returns all needs containing the string
    *
    * @param searchText the text to find in the need names
    *
    * @return ArrayList<needs>: the list of all needs containing the search string
              prioritizes needs that start with searchText
    *         if search string is null all needs should be returned
    **/
    public ArrayList<Needs> searchPartialName(String searchText) {
        ArrayList<Needs> needArrayList = new ArrayList<>();
        searchText = searchText.toLowerCase();

        for (Needs need : CupboardList) {
            if (searchText == null || need.getName().toLowerCase().contains(searchText)) {
                if(need.getName().toLowerCase().startsWith(searchText)){
                    needArrayList.add(0, need);
                    continue;
                }
                needArrayList.add(need);
            }
        }
        return needArrayList;
    }

    /**
    * Updates the cost, type, and quantity of the Need according to its respective name in the list.
    *
    * @param name The name of the Need that is to be updated.
    * @param newCost The new cost of the Need that is being updated. 
    * @param newQuantity The new quantity of the Need that is being updated. 
    * @param newType The new type of the Need that is being updated. 
    *
    * @return Returns true if the need is updated successfully, false otherwise.
    **/
    public boolean updateNeed(String name, double newCost, String newType, Integer newQuantity) {
        for (Needs need : CupboardList) {
            if(need.getName().equals(name)) {
                if (newCost >= 0.0) {
                    need.setCost(newCost);
                } 

                if (newType != null) {
                    need.setType(newType);    
                }

                if (newQuantity >= 0) {
                    need.setQuantity(newQuantity);
                }
                return true;
            }
        }
        return false;
    }


    /**
    * Function that returns all the Needs within the Cupboard
    * @return ArrayList of all the Needs within the Cupboard
    **/
    public ArrayList<Needs> getCupboard() {
        return CupboardList;
    }
}