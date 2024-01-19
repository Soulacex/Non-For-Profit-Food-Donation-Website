package com.ufund.api.ufundapi.model.accounts;

public class ManagerAccount implements UserAccount{
    private final AccountType type = AccountType.Manager;
    private final String username = "admin";

    public AccountType getType() {
        return type;
    }
    public String getUsername() {
        return username;
    }
}
