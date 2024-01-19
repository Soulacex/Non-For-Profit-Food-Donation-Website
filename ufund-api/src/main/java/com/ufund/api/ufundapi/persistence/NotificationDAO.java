package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.notifications.Notification;

public interface NotificationDAO {

    /**
     * Create a notification object front a need and quantity donated by a helper.
     * @param notification The notification to be created
     * @return Returns new Nontification created.
     */
    Notification createNotification(Notification notification) throws IOException;
    
    /**
    * Function that returns all the notifications that has been created.
    * @return Returns ArrayList of all the Notifications created.
    **/
    Notification[] getAllNotifications() throws IOException;

    /**
    * Function that returns the notification with the specified id.
    * @return Returns the notification with the specified id.
    **/
    Notification getNotification(int Id) throws IOException;
}
