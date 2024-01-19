package com.ufund.api.ufundapi.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.model.Cupboard;
import com.ufund.api.ufundapi.model.FundingBasket;

public class HelperAccount implements UserAccount{
    private final AccountType type = AccountType.Helper;
    private FundingBasket basket;

    @JsonProperty("username") private String username;

    public HelperAccount(@JsonProperty("username") String username) {
        this.username = username;
        basket = new FundingBasket(new Cupboard());
    }
    public AccountType getType() {
        return type;
    }
    public String getUsername() {
        return username;
    }
    public FundingBasket getBasket() {
        return basket;
    }
}
