package co.edu.icesi.demo.enums;

public enum PathRoutesDefault {
    ACCOUNT_DEFAULT("/Accounts"),
    USER_DEFAULT("/Users"),
    ROLE_DEFAULT("/Roles");

    private final String path;

    PathRoutesDefault(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}