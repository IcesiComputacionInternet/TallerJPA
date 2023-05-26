package co.com.icesi.jpataller.error.util;

import co.com.icesi.jpataller.error.exception.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class IcesiExceptionBuilder {

    public static Supplier<IcesiException> createIcesiException(String message, String details) {
        return () -> new IcesiException(message, createIcesiError(HttpStatus.BAD_REQUEST, details));
    }

    public static IcesiException createIcesiException(String message, HttpStatus httpStatus,String details) {
        return new IcesiException(message, createIcesiError(httpStatus, details));
    }

    public static IcesiError createIcesiError(HttpStatus httpStatus,String details){
        return IcesiError.builder()
                .status(httpStatus)
                .details(details)
                .build();

    }



}
