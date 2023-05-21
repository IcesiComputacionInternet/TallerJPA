package co.edu.icesi.demo.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class IcesiError {

    private HttpStatus status;
    private List<IcesiErrorDetail> details;

}
