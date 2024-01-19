package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.HashMap;
import com.ufund.api.ufundapi.model.Needs;

public interface FundingBasketDAO {
    
    /**
     *gets the list of all the needs that's currently in the funding basket
     * @return Returns List of Needs in funding basket
     **/
    HashMap<String, Integer> getSupportedNeeds() throws IOException; 

    /**
     * Checks if need is in Cupboard then adds need to FundingBasket
     * @param name_need The name of the need being added
     * @return Returns the need added to funding basket if need is in cupboard
     *         Returns null if need is not in cupboard
     **/
    Needs addSupportedNeed(String name, Integer quantity) throws IOException;  

    /**
     * Checks if need is in fundingbasket then removes the need
     * @param name_need The name of the need being removed
     * @return Returns the need removed to funding basket if need is in cupboard
     *         Returns null if need is not in funding basket
     **/
    Needs removeSupportedNeed(String name) throws IOException;

    /**
     *Checks out and clears all the need within the funding basket and clears the need
     * @return Returns true when method is complete
     **/
    boolean checkOut() throws IOException;
}
