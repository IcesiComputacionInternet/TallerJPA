package com.example.TallerJPA.error.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class IcesiError {
    private HttpStatus status;
    private List<ErrorDetail> details;

}
