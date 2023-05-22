package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/*@Data
@AllArgsConstructor*/
@Builder
@Getter
public class IcesiError {
    private HttpStatus status;
    private List<IcesiErrorDetail> details;
}
