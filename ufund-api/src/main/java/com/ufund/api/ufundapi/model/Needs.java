package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Needs {

    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("type") private String type;
    @JsonProperty("quantity") private int quantity;

    public Needs(@JsonProperty("name") String name, @JsonProperty("cost") double cost, 
    @JsonProperty("type") String type, @JsonProperty("quantity") Integer quantity) {
        this.name = name;
        this.cost = cost;
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Gets name value
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Gets cost value
     * @return cost
     */
    public double getCost() {
        return cost;
    }
    /**
     * Gets type value
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * Gets quantity value
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets name value
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets cost value
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    /**
     * Sets type value
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Sets quantity value
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isSame = false;

        if (object != null && object instanceof Needs)
        {
            isSame = this.name.equals(((Needs) object).getName());
        }
        return isSame;
    }
}