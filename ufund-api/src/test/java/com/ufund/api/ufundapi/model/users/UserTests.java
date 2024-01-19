package com.ufund.api.ufundapi.model.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.Needs;

public class UserTests {
    private Users user;
    private Cupboard cupboard;
    private Needs need_1;
    private Needs  need_2;
    private Needs need_3;

    @BeforeEach
    public void setUp() {
        cupboard = new Cupboard();
        user = new Users(cupboard);

        //Setup
        cupboard.createNeed("Beans", 5, "Canned", 937);
        cupboard.createNeed("Carrots", 6, "Fresh", 53);
        cupboard.createNeed("Corn", 3, "Canned", 4);
        
        need_1 = cupboard.searchNameNeed("Beans");
        need_2 = cupboard.searchNameNeed("Carrots");
        need_3 = cupboard.searchNameNeed("Corn");
    }

    @Test
    public void browseNeeds(){
        //Invoke
        ArrayList<Needs> expected = new ArrayList<>();
        expected.add(need_3);
        expected.add(need_2);
        ArrayList<Needs> test = new ArrayList<>();
        test = user.browseNeeds("C");

        //Analyze
        assertEquals(expected, test);
    }

    @Test
    public void getAllNeeds(){
        //Invoke
        Needs[] test = user.getAllNeeds();

        //Analyze
        assertEquals(3, test.length);
    }
}
