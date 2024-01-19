package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.logging.Logger;
import com.ufund.api.ufundapi.model.*;


@Component
public class CupboardFileDAO implements CupboardDAO {
    private static final Logger LOG = Logger.getLogger(CupboardDAO.class.getName());
    Map<String, Needs> Needs;   // Provides a local cache of the hero objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Hero
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Hero File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CupboardFileDAO(@Value("${Cupboard.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the cupboard from the file
    }

    /**
     * Generates an array of {@linkplain Hero heroes} from the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private Needs[] getNeedsArray() {
        return getNeedsArray(null);
    }

    /**
     * Generates an array of {@linkplain Hero heroes} from the tree map for any
     * {@linkplain Hero heroes} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Hero heroes}
     * in the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private Needs[] getNeedsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Needs> NeedArrayList = new ArrayList<>();

        for (Needs need : Needs.values()) {
            if (containsText == null || need.getName().contains(containsText)) {
                NeedArrayList.add(need);
            }
        }

        Needs[] needArray = new Needs[NeedArrayList.size()];
        NeedArrayList.toArray(needArray);
        return needArray;
    }

    /**
     * Saves the {@linkplain Hero heroes} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Hero heroes} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Needs[] needArray = getNeedsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),needArray);
        return true;
    }

    /**
     * Loads {@linkplain Hero heroes} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        Needs = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of heroes
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Needs[] needArray = objectMapper.readValue(new File(filename),Needs[].class);

        // Add each hero to the tree map and keep track of the greatest id
        for (Needs need : needArray) {
            Needs.put(need.getName(),need);
        }
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public ArrayList<Needs> getCupboard() {
        synchronized(Needs) {
            ArrayList<Needs> NeedArrayList = new ArrayList<>();

        for (Needs need : Needs.values()) {
            NeedArrayList.add(need);
        }
        return NeedArrayList;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public ArrayList<Needs> searchPartialName(String containsText) {
        synchronized(Needs) {
        ArrayList<Needs> NeedArrayList = new ArrayList<>();
        for (Needs need : Needs.values()) {
            if (containsText == null || need.getName().contains(containsText)) {
                NeedArrayList.add(need);
            }
        }
        return NeedArrayList;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Needs searchNameNeed(String needString) {
        synchronized(Needs) {
            if (Needs.containsKey(needString))
                return Needs.get(needString);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Needs createNeed(String name, double cost, String type, Integer quantity) throws IOException{
        synchronized(Needs) {
            // We create a new hero object because the id field is immutable
            // and we need to assign the next unique id
            Needs newNeed = new Needs(name,cost,type,quantity);
            Needs.put(newNeed.getName(),newNeed);
            save(); // may throw an IOException
            return newNeed;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean updateNeed(String name, double newCost, String newType, Integer newQuantity) throws IOException {
        synchronized(Needs) {
            if (Needs.containsKey(name) == false)
                return false;  // hero does not exist

            Needs newNeed = new Needs(name,newCost,newType,newQuantity);
            Needs.put(newNeed.getName(),newNeed);
            save(); // may throw an IOException
            return true;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteNeeds(String name) throws IOException {
        synchronized(Needs) {
            if (Needs.containsKey(name)) {
                Needs.remove(name);
                return save();
            }
            else
                return false;
        }
    }
}
