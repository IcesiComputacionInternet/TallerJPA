package co.com.icesi.icesiAccountSystem.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AccountSystemErrorDetail {
    private String errorCode;
    private String errorMessage;
}
