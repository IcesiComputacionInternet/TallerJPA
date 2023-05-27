package com.edu.icesi.demojpa.Enum;

public enum AccountRole {
    ADMIN("ADMIN"),
    BANK("BANK"),
    USER("USER");

    private final String role;

    private AccountRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
