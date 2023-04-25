package co.edu.icesi.tallerjpa.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class IcesiError {

    private HttpStatus httpStatus;
    private List<IcesiErrorDetail> details;

}
