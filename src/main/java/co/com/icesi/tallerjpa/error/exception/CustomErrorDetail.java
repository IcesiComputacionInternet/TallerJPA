package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CustomErrorDetail {

    private String errorCode;
    private String errorMessage;


}
