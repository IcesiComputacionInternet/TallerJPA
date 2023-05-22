package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class IcesiException extends RuntimeException {
    private final IcesiError error;
    public IcesiException(String message, IcesiError error) {
        super(message);
        this.error = error;
    }
}
