package co.com.icesi.tallerjpa.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class CustomError {

    private HttpStatus status;
    private List<CustomErrorDetail> details;

}
