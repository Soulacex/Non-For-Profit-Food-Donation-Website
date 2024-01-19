package com.ufund.api.ufundapi.model.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    @JsonProperty("Id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("need") String need;
    @JsonProperty("quantity") int quantity;

    public Notification(@JsonProperty("Id") int id,@JsonProperty("username") String username, @JsonProperty("need") String need, @JsonProperty("quantity") int quantity){
        this.id = id;
        this.username = username;
        this.need = need;
        this.quantity = quantity;
    }

    /**
     * Gets id value
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets username value
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets need name value
     * @return need
     */
    public String getNeed() {
        return need;
    }

    /**
     * Gets quantity value
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return username + " donated " + quantity + " of " + need + "!";
    }

}
