package com.Icesi.TallerJPA.error.exception;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class IcesiError {
    private ErrorConstants code;
    private String message;
}
