package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.model.SupportedNeed;

public class HelperFileDAOTest {
    HelperFileDAO helperFileDAO;
    CupboardFileDAO CupboardFileDAO;
    Map<String, SupportedNeed> testSupported;
    Needs[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupAccountsFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        testNeeds = new Needs[3];
        testNeeds[0] = new Needs("Beans",1,"Canned",10);
        testNeeds[1] = new Needs("Carrots",3,"Fresh",5);
        testNeeds[2] = new Needs("Apple Sauce",90,"Jarred",20);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
            .readValue(new File("test2.txt"),Needs[].class))
                .thenReturn(testNeeds);
        CupboardFileDAO = new CupboardFileDAO("test2.txt",mockObjectMapper);

        testSupported = new HashMap<>();
        testSupported.put("Beans", new SupportedNeed("helper1", "Beans", 10));
        testSupported.put("Carrots", new SupportedNeed("helper1", "Carrots", 5));
        testSupported.put("Apple Sauce", new SupportedNeed("helper2", "Apple Sauce", 20));

        when(mockObjectMapper.readValue(new File("test.txt"), Map.class))
            .thenReturn(testSupported);
        helperFileDAO = new HelperFileDAO("test.txt","test2.txt", mockObjectMapper);
    }

    // @Test
    // public void testGetSupportedNeed() throws IOException{
    //     // Invoke
    //     Map<String, SupportedNeed> supported = helperFileDAO.getSupportedNeeds("helper1");

    //     // Analyze
    //     assertEquals(supported.size(),testSupported.size());
    // }

}
