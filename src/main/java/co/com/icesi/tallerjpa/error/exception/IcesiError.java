package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IcesiError {
    private String message;
}
