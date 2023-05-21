package co.com.icesi.TallerJpa.enums;

public enum AccountType {
    NORMAL("Normal"),
    DEPOSIT_ONLY("Deposit only");

    private final String type;

    private AccountType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
