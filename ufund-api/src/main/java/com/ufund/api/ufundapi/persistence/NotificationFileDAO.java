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
import com.ufund.api.ufundapi.model.notifications.Notification;

/**
 * Implements the functionality for JSON file-based peristance for Notification
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author 7G FundsInHighPlaces
 */
@Component
public class NotificationFileDAO implements NotificationDAO {
    private static final Logger LOG = Logger.getLogger(NotificationFileDAO.class.getName());
    Map<Integer, Notification> notifications;   // Provides a local cache of the notification objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Account
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new Notification
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Notification File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public NotificationFileDAO(@Value("${Notification.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the cupboard from the file
    }

    /**
     * Generates the next id for a new {@linkplain Notification notification}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Notification notification} from the tree map
     * 
     * @return  The array of {@link Notification notification}, may be empty
     */
    private Notification[] getNotificationArray() {
        return getNotificationArray(-1);
    }

    /**
     * Generates an array of {@linkplain Notification notification} from the tree map for any
     * {@linkplain Notification notification} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Notification notification}
     * in the tree map
     * 
     * @return  The array of {@link Notification notification}, may be empty
     */
    private Notification[] getNotificationArray(int id) { // if containsText == null, no filter
        ArrayList<Notification> notificationArrayList = new ArrayList<>();

        for (Notification notification : notifications.values()) {
            if (id == -1 || notification.getId() == id) {
                notificationArrayList.add(notification);
            }
        }

        Notification[] notificationArray = new Notification[notificationArrayList.size()];
        notificationArrayList.toArray(notificationArray);
        return notificationArray;
    }

    /**
     * Saves the {@linkplain Notification notification} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Notification notification} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Notification[] notificationArray = getNotificationArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),notificationArray);
        return true;
    }

    /**
     * Loads {@linkplain Notification notification} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        notifications = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of notifications
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Notification[] notificationArray = objectMapper.readValue(new File(filename),Notification[].class);

        // Add each notification to the tree map and keep track of the greatest id
        for (Notification notification : notificationArray) {
            notifications.put(notification.getId(),notification);
            if (notification.getId() > nextId)
                nextId = notification.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Notification createNotification(Notification notification) throws IOException{
        synchronized(notifications) {
            Notification newNotification = new Notification(nextId(), notification.getUsername(), notification.getNeed(), notification.getQuantity());
            notifications.put(newNotification.getId(), newNotification);
            save();
            return newNotification;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Notification[] getAllNotifications() {
        synchronized(notifications) {
            return getNotificationArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Notification getNotification(int id) {
        synchronized(notifications) {
            if (notifications.containsKey(id))
                return notifications.get(id);
            else
                return null;
        }
    }
}
