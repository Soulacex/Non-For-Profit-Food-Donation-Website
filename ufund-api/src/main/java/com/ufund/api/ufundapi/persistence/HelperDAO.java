package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.model.SupportedNeed;

public interface HelperDAO {
    /**
     * Checks if need is in Cupboard then adds need to FundingBasket
     * 
     * @param need_name The name of the need being added
     * 
     * @return Returns the need added to funding basket if need is in cupboard
     *         Returns null if need is not in cupboard
     **/
    Needs addSupportedNeed(SupportedNeed supportedNeed) throws IOException;

    
    /**
     * Checks if need is in fundingbasket then removes the need
     * @param need_name The name of the need being removed
     * @return Returns the need removed to funding basket if need is in cupboard
     *         Returns null if need is not in funding basket
     **/
    Needs removeSupportedNeed(SupportedNeed supportedNeed) throws IOException;


    /**
     *Checks out and clears all the need within the funding basket and clears the need
     * @return Returns true when method is complete
     **/
    boolean checkOut(String username) throws IOException;

    /**
    * calls searchPartialName method in Cupboard class to search for needs
    *
    * @param String searchText the text to find in the need names
    *
    * @return ArrayList<needs>: the list of all needs containing the search string
              prioritizes needs that start with searchText
    *         if search string is null all needs should be returned
    **/
    ArrayList<Needs> browseNeeds(String searchText) throws IOException;

    /**
    * calls getCupboard() method in Cupboard class to get all needs in Cupboard
    *
    * @return ArrayList<needs>: the list of all needs in cupboard
    **/
    public Map<String, SupportedNeed> getSupportedNeeds(String username) throws IOException;


}
