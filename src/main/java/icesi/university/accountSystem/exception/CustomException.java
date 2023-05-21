package icesi.university.accountSystem.exception;

import java.io.Serial;

public class CustomException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private String err_code;
    public CustomException(String message,String err_code) {
        super(message);
        this.err_code = err_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public CustomException(String message) {
        super(message);
    }

}
