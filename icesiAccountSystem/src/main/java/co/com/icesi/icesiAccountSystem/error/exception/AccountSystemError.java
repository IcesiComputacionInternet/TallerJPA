package co.com.icesi.icesiAccountSystem.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSystemError {

    private HttpStatus status;
    private List<AccountSystemErrorDetail> details;

}
