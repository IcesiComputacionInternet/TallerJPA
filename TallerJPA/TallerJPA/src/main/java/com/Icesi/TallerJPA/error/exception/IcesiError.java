package com.Icesi.TallerJPA.error.exception;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
@AllArgsConstructor
public class IcesiError {
    private ErrorConstants code;
    private String message;
}
