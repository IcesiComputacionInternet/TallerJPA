package co.com.icesi.demojpa.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class IcesiError {

    private HttpStatus status;
    private String details;


}
