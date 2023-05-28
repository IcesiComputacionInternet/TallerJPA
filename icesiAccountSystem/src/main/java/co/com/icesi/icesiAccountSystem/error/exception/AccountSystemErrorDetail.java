package co.com.icesi.icesiAccountSystem.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSystemErrorDetail {
    private String errorCode;
    private String errorMessage;
}
