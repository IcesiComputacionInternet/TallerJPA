package com.icesi.TallerJPA.enums;

public enum IcesiAccountType {
    DEPOSIT("Deposit"),
    DEFAULT("Default");

    private final String value;

    IcesiAccountType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
