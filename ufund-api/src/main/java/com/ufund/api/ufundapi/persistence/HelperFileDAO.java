package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Needs;
import com.ufund.api.ufundapi.model.SupportedNeed;

/**
 * Implements the functionality for JSON file-based peristance for Account
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author 7G FundsInHighPlaces
 */
@Component
public class HelperFileDAO implements HelperDAO {
    private static final Logger LOG = Logger.getLogger(HelperFileDAO.class.getName());
    Map<String, Map<String, SupportedNeed>> fundingBasket;   // Provides a local cache of the account objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Account
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to
    private String file2;
    private CupboardDAO cupboardDAO;

    /**
     * Creates am Account File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public HelperFileDAO(@Value("${Helper.file}") String filename,@Value("${Cupboard.file}") String filename2, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.file2 = filename2;
        this.objectMapper = objectMapper;
        cupboardDAO = new CupboardFileDAO(file2, new ObjectMapper());
        load();  // load the accounts from the file
    }

    /**
     * Saves the {@linkplain Hero heroes} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Hero heroes} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), fundingBasket);
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
        fundingBasket = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of heroes
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        fundingBasket = objectMapper.readValue(new File(filename), new TypeReference<Map<String, Map<String, SupportedNeed>>>() {});
 
        return true;
    }

    @Override
    public Needs addSupportedNeed(SupportedNeed supportedNeed) throws IOException {
        synchronized(fundingBasket) {
            String username = supportedNeed.getUsername();
            String need_name = supportedNeed.getSupportedNeedName();
            Needs need = cupboardDAO.searchNameNeed(need_name);
            int total = 0;
            Map<String, SupportedNeed> basket = fundingBasket.get(username);
            if (basket == null) {
                basket = new HashMap<>();
                fundingBasket.put(username, basket);
            }
            if (fundingBasket.get(username).containsKey(need_name)) {
                total = fundingBasket.get(username).get(need_name).getSupportedNeedQuantity();
            }
            if (need != null && total < need.getQuantity()) {
                if (basket.containsKey(need_name)) {
                    basket.get(need_name).setSupportedNeedQuantity(basket.get(need_name).getSupportedNeedQuantity() + 1);
                    basket.put(need_name, basket.get(need_name));
                } else {
                    basket.put(need_name, new SupportedNeed(username, need_name, 1));
                }
                save();
                return need;
            }
            return null;
        }
    }

    @Override
    public Needs removeSupportedNeed(SupportedNeed supportedNeed) throws IOException {
        synchronized(fundingBasket) {
            String need_name = supportedNeed.getSupportedNeedName();
            String username = supportedNeed.getUsername();
            Needs need = cupboardDAO.searchNameNeed(need_name);
            Map<String, SupportedNeed> basket = fundingBasket.get(username);
            if (basket != null) {
                if (basket.containsKey(need_name)){
                    int current = basket.get(need_name).getSupportedNeedQuantity();
                    if (current == 1) {
                        basket.remove(need_name);
                    } else {
                        basket.get(need_name).setSupportedNeedQuantity(current - 1);
                    }
                    save();
                    return need;
                }
            }
            return null;
        }
    }

    @Override
    public Map<String, SupportedNeed> getSupportedNeeds(String username) throws IOException {
        synchronized(fundingBasket) {
            if (fundingBasket.containsKey(username)) {
                return fundingBasket.get(username);
            }
            return null;
        }
    }

    @Override
    public ArrayList<Needs> browseNeeds(String searchText) throws IOException {
        return cupboardDAO.searchPartialName(searchText);
    }

    @Override
    public boolean checkOut(String username) throws IOException {
        synchronized(fundingBasket) {
            Map<String, SupportedNeed> basket = fundingBasket.get(username);
            if (basket == null) {
                return false;
            }
            for (Map.Entry<String, SupportedNeed> entry : basket.entrySet()) {
                Needs need = cupboardDAO.searchNameNeed(entry.getKey());
                String name = need.getName();
                double cost = need.getCost();
                String type = need.getType();
                int quantity = entry.getValue().getSupportedNeedQuantity();
                if (need != null) {
                    int updatedQuantity = need.getQuantity() - quantity;
                    if (updatedQuantity <= 0) {
                        cupboardDAO.deleteNeeds(name);
                    } else {
                        cupboardDAO.updateNeed(name, cost, type, updatedQuantity);
                    }
                }
            }
            fundingBasket.remove(username);
            save();
            return true;
        }
    }
}
