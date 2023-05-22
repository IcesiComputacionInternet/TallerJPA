package co.com.icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor
public class ApplicationError {
    private HttpStatus status;
    private List<ErrorDetail> details;
}
