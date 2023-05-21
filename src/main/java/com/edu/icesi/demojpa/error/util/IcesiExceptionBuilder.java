package com.edu.icesi.demojpa.error.util;

import com.edu.icesi.demojpa.error.exception.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class IcesiExceptionBuilder {
    public IcesiException noPermissionException(String message){
        IcesiError error = createIcesiError(HttpStatus.UNAUTHORIZED, new DetailBuilder(ErrorCode.ERROR_401,
                ErrorCode.ERROR_401.getMessage()));
        return new IcesiException(message, error);
    }

    public IcesiException notFoundException(String message, String field1, String field2, String field3){
        IcesiError error = createIcesiError(HttpStatus.NOT_FOUND, new DetailBuilder(ErrorCode.ERROR_404,
                ErrorCode.ERROR_404.getMessage().formatted(field1, field2, field3)));
        return new IcesiException(message, error);
    }

    public IcesiException badRequestException(String message, String field1){
        IcesiError error = createIcesiError(HttpStatus.BAD_REQUEST, new DetailBuilder(ErrorCode.ERROR_400,
                ErrorCode.ERROR_400.getMessage().formatted(field1)));
        return new IcesiException(message, error);
    }

    public IcesiException duplicatedValueException(String message, String field1){
        IcesiError error = createIcesiError(HttpStatus.CONFLICT, new DetailBuilder(ErrorCode.ERR_DUPLICATED,
                ErrorCode.ERR_DUPLICATED.getMessage().formatted(field1)));
        return new IcesiException(message, error);
    }

    public static IcesiError createIcesiError(HttpStatus httpStatus, DetailBuilder... details) {
        return IcesiError.builder()
                .status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(IcesiExceptionBuilder::mapToIcesiErrorDetail)
                                .toList()
                )
                .build();
    }

    private static IcesiErrorDetail mapToIcesiErrorDetail(DetailBuilder detailBuilder) {
        return IcesiErrorDetail.builder()
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();
    }
}
