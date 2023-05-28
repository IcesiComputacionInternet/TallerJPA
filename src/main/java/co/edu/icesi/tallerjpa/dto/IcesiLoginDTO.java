package co.edu.icesi.tallerjpa.dto;



public record IcesiLoginDTO(String username, String password) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
