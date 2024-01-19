
package com.ufund.api.ufundapi.model.users;
import java.util.ArrayList;

import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;

/**
* Displays the functionality for all users of the food bank
* 
* @author Kelvin Ng, Mason Zeng, Charles von Goins II, Sam Pepperman
*/
public class Users {
    private Cupboard cupboard;

    /**
     * Displays the functionality of a user
     *
     *@param cupboard_object the cupboard that the user has access to
    **/
    public Users(Cupboard cupboard_object){
        cupboard = cupboard_object; 
    }

    /**
    * calls searchPartialName method in Cupboard class to search for needs
    *
    * @param String searchText the text to find in the need names
    *
    * @return ArrayList<needs>: the list of all needs containing the search string
    prioritizes needs that start with searchText
    *         if search string is null all needs should be returned
    **/
    public ArrayList<Needs> browseNeeds(String searchText){
        ArrayList<Needs> needArrayList = new ArrayList<>();
        needArrayList = cupboard.searchPartialName(searchText);
        return needArrayList;
    }

    /**
     * calls getCupboard() method in Cupboard class to get all needs in Cupboard
    *
    * @return ArrayList<needs>: the list of all needs in cupboard
    **/
    public Needs[] getAllNeeds(){
        ArrayList<Needs> needArrayList = cupboard.getCupboard();
        Needs[] needsArray = new Needs[needArrayList.size()];
        return needArrayList.toArray(needsArray);
    }
}