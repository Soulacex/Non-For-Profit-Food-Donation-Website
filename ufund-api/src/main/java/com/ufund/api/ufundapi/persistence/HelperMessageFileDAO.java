package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.HelperMessage.HelperMessage;


@Component
public class HelperMessageFileDAO implements HelperMessageDAO{
    private static final Logger LOG = Logger.getLogger(CupboardDAO.class.getName());
    Map<Integer, HelperMessage> Messages;   // Provides a local cache of the hero objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Hero
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new HelperMessage
    private String filename;    // Filename to read from and write to

    /**
     * Creates a helper File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public HelperMessageFileDAO(@Value("${Message.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the cupboard from the file
    }


    /**
     * Generates the next id for a new {@linkplain HelperMessage message}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

     /**
     * Generates an array of {@linkplain HelperMessage messages} from the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private HelperMessage[] getMessageArray() {
        return getMessageArray(null);
    }

    /**
     * Generates an array of {@linkplain HelperMessage messages} from the tree map for any
     * {@linkplain HelperMessage message} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Hero heroes}
     * in the tree map
     * 
     * @return  The array of {@link Hero heroes}, may be empty
     */
    private HelperMessage[] getMessageArray(String containsText) { // if containsText == null, no filter
        ArrayList<HelperMessage> MessageArrayList = new ArrayList<>();

        for (HelperMessage message : Messages.values()) {
            if (containsText == null || message.getName().contains(containsText)) {
                MessageArrayList.add(message);
            }
        }

        HelperMessage[] messageArray = new HelperMessage[MessageArrayList.size()];
        MessageArrayList.toArray(messageArray);
        return messageArray;
    }

    /**
     * Saves the {@linkplain HelperMessage messages} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link HelperMessage messages} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        HelperMessage[] messagesArray = getMessageArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),messagesArray);
        return true;
    }

    /**
     * Loads {@linkplain HelperMessage messages} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        Messages = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of messages
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        HelperMessage[] messageArray = objectMapper.readValue(new File(filename),HelperMessage[].class);

        // Add each hero to the tree map and keep track of the greatest id
        for (HelperMessage message : messageArray) {
            Messages.put(message.getId(),message);
            if (message.getId() > nextId)
                nextId = message.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }



    /**
    ** {@inheritDoc}
     */
    @Override
    public HelperMessage creatHelperMessage(HelperMessage helperMessage) throws IOException {
        synchronized(Messages) {
            // We create a new hero object because the id field is immutable
            // and we need to assign the next unique id
            HelperMessage newMessage = new HelperMessage(nextId(),helperMessage.getName(),helperMessage.getMessage());
            Messages.put(newMessage.getId(),newMessage);
            save(); // may throw an IOException
            return newMessage;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public HelperMessage searchMessage(int Id){
        synchronized(Messages) {
            for (HelperMessage message : Messages.values()) {
                if(message.getId()==Id){
                    return message;
                }
            }
            return null;
        }
    }

    @Override
    public HelperMessage[] getAllHelperMessages(){
        synchronized(Messages){
            return getMessageArray();
        }
    }
    
}
