package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@AllArgsConstructor
public class IcesiErrorDetail {

    private String errorCode;
    private String errorMessage;
}