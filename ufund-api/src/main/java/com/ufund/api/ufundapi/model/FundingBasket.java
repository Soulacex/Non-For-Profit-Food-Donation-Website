package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.persistence.FundingBasketDAO;

/**
 * FundingBasket where users can checkout needs that they want to donate
 * 
 * @author Kelvin Ng, Mason Zeng, Charles von Goins II, Sam Pepperman
 */
@Repository
public class FundingBasket implements FundingBasketDAO {
    private List<Needs> supportedNeeds;
    private Cupboard cupboard;
    private HashMap<String, Integer> donationQuantity;
    @JsonProperty("name") private String name;
    @JsonProperty("quantity") private Integer quantity;

    /**
    * Creates the funding basket of a helper
    *
    *@param cupboard_object the cupboard that the donator has access to
    **/
    public FundingBasket(Cupboard cupboardObject) {
        this.supportedNeeds = new ArrayList<>();
        cupboard = cupboardObject;
        donationQuantity = new HashMap<>();
    }

    /**
    * Function that returns all the Needs within the funding basket
    *
    * @return ArrayList of all the Needs within the funding basket
    **/
    public HashMap<String, Integer> getSupportedNeeds() {
        return donationQuantity;
    }

    /**
    * Users adds a need to the funding basket
    *
    * @param name name of need the need that is being added to the funding basket
    * @param quant number of that need to check out
    * @return Need that is added to the funding basket
    *         Null if need is not in Cupboard
    **/
    public Needs addSupportedNeed(@JsonProperty("name") String name, @JsonProperty("quantity") Integer quantity){
        Needs need = cupboard.searchNameNeed(name);
        int total = quantity;
        if (donationQuantity.containsKey(name)) {
            total += donationQuantity.get(name);
        } 
        if (need != null && total <= need.getQuantity()){
            if (donationQuantity.containsKey(name)) {
                donationQuantity.put(name, donationQuantity.get(name) + quantity);
            } else {
                donationQuantity.put(name, quantity);
                supportedNeeds.add(need);
            }
            return need;
        }
        return null;
    }

    /**
    * Users can removes a specific need from the funding basket
    *
    * @param name the name of the need that is being removed from the funding basket
    * @return Need that is removed from the funding basket
    *         Null if need is not in funding basket
    **/
    public Needs removeSupportedNeed(String need_name) {
        Needs need = cupboard.searchNameNeed(need_name);
        if(supportedNeeds.contains(need)){
            supportedNeeds.remove(need);
            donationQuantity.remove(need_name);
            return need;
        }
        return null;
    }

    /**
    * Users can checkout the needs from funding basket
    *
    *@return true when checkout works
    **/
    public boolean checkOut(){
        for(int i = 0; i < supportedNeeds.size(); i++){
            Needs need = supportedNeeds.get(i);
            String name = need.getName();
            double cost = need.getCost();
            String type = need.getType();
            int quantity = donationQuantity.get(name);

            Needs cupboardNeeds = cupboard.searchNameNeed(name);
            if (cupboardNeeds != null) {
                int updatedQuantity = (cupboard.searchNameNeed(name)).getQuantity() - quantity;
                cupboard.updateNeed(name, cost, type, updatedQuantity);
            }
        }
        supportedNeeds.clear();
        donationQuantity.clear();
        return true;
    }
}