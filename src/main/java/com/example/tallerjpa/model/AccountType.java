package com.example.tallerjpa.model;

public enum AccountType {
    DEPOSIT("Deposit"),
    DEFAULT("Default");

    private final String value;

    AccountType (String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}