package com.edu.icesi.TallerJPA.error.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class IcesiError {

    private HttpStatus status;
    private List<IcesiErrorDetail> details;

}
