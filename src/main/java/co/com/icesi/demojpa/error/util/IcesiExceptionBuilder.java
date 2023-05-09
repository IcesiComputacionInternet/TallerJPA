package co.com.icesi.demojpa.error.util;

import co.com.icesi.demojpa.error.exception.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class IcesiExceptionBuilder {

    public static Supplier<IcesiException> createIcesiException(String message, String details) {
        return () -> new IcesiException(message, createIcesiError(message, HttpStatus.BAD_REQUEST, details));
    }

    public static Supplier<IcesiException> createIcesiException(String message, HttpStatus httpStatus,String details) {
        return () -> new IcesiException(message, createIcesiError(message, httpStatus, details));
    }

    public static IcesiError createIcesiError(String message, HttpStatus httpStatus,String details){
        return IcesiError.builder().status(httpStatus)
                .details(
                        details
                ).build();
    }



}
