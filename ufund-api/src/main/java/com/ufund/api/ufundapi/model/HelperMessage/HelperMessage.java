package com.ufund.api.ufundapi.model.HelperMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HelperMessage {
    @JsonProperty("Id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("message") private String message;

     public HelperMessage(@JsonProperty("Id") int id,@JsonProperty("username") String name, @JsonProperty("message") String message){
        this.id = id;
        this.username = name;
        this.message = message;
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
    public String getName() {
        return username;
    }

    /**
     * Gets message value
     * @return message
     */
    public String getMessage() {
        return message;
    }

    
}
