package co.com.icesi.icesiAccountSystem.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class AccountSystemError {

    private HttpStatus status;
    private List<AccountSystemErrorDetail> details;

}
