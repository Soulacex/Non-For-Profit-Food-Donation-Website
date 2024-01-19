package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportedNeed {
    @JsonProperty("username") private String username;
    @JsonProperty("need_name") private String needName;
    @JsonProperty("quantity") private int quantity;

    public SupportedNeed(@JsonProperty("username") String username, @JsonProperty("need_name") String need_name, @JsonProperty("quantity") int quant){
        this.username = username;
        this.needName = need_name;
        this.quantity = quant;
    }

    public String getUsername(){
        return this.username;
    }
    public String getSupportedNeedName(){
        return needName;
    }
    public int getSupportedNeedQuantity(){
        return this.quantity;
    }

    public void setQuantity(int newQuant){
        this.quantity = newQuant;
    }
    public void setSupportedNeedQuantity(int newQuantity){
        this.quantity = newQuantity;
    }
}
