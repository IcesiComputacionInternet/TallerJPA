package com.edu.icesi.demojpa.Enum;

public enum AccountType {
    DEPOSIT_ONLY("Deposit only"),
    NORMAL("Normal");

    private final String type;

    private AccountType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

}
