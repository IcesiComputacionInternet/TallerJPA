package co.com.icesi.TallerJpa.exceptions.icesiRoleExceptions;

public class RoleNameAlreadyInUseException extends RuntimeException{
    public RoleNameAlreadyInUseException(){
        super("RoleNameAlreadyInUseException: The name role is already in use");
    }
}
