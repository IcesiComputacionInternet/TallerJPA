package co.com.icesi.TallerJpa.exceptions.icesiUserExceptions;

public class RoleCantBeNullException extends RuntimeException{
    public RoleCantBeNullException(){
        super("RoleCantBeNullException: The role in user cant be null");
    }
}
