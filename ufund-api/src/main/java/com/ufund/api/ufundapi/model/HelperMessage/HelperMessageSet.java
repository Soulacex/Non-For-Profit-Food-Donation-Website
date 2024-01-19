package com.ufund.api.ufundapi.model.HelperMessage;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;


public class HelperMessageSet {
    private ArrayList<HelperMessage> MessageList;
    @JsonProperty("Id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("message") private String message;

    public HelperMessageSet(){
        MessageList = new ArrayList<>();
    }

    protected HelperMessage createHelperMessage( @JsonProperty("Id") int id, @JsonProperty("username") String username,@JsonProperty("message") String message){
        //Verify unique ID
        for (HelperMessage helperMessage : MessageList) {
            if (helperMessage.getId()==id){
                return null;
            }
        }
        //Verify valid name
        if (username==""){
            return null;
        }
        //Verify valid message
        if (message==""){
            return null;
        }
        HelperMessage  newMessage = new HelperMessage(id,username, message);
        MessageList.add(newMessage);
        return newMessage;
    }

    protected HelperMessage searchMessage(int id){
        for(int i=0;i<MessageList.size();i++){
            if(MessageList.get(i).getId()==id){
                return MessageList.get(i);
            }
        }
        return null;
    }
    
}
