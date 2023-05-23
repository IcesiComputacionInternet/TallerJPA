package com.icesi.TallerJPA.error.util;

import com.icesi.TallerJPA.error.exception.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Builder
public class IcesiExceptionBuilder {


    public static IcesiError createIcesiError(String message, HttpStatus httpStatus, DetailBuilder... details) {
        return IcesiError.builder().status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(IcesiExceptionBuilder::mapToIcesiErrorDetail)
                                .toList()
                ).build();
    }

    public static IcesiErrorDetail mapToIcesiErrorDetail(DetailBuilder detailBuilder) {
        return IcesiErrorDetail.builder()
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();

    }

    public IcesiException exceptionDuplicate(String message, String field1, String field2, String field3) {

        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_DUPLICATED.getCode())
                .errorMessage(ErrorCode.ERR_DUPLICATED.getMessage().formatted(field1, field2, field3))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiException exceptionNotFound(String message, String field1) {

        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_404.getCode())
                .errorMessage(ErrorCode.ERR_404.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiException exceptionDontValue(String message, String field1) {

        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_DONT_VALUE.getCode())
                .errorMessage(ErrorCode.ERR_DONT_VALUE.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiException exceptionDontDisable(String message, String field1) {

        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_DONT_DISABLE.getCode())
                .errorMessage(ErrorCode.ERR_DONT_DISABLE.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiException exceptionAccountInactive(String message, String field1){

        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_ACCOUNT_INACTIVE.getCode())
                .errorMessage(ErrorCode.ERR_ACCOUNT_INACTIVE.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiException exceptionUnauthorized(String message, String user) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_UNAUTHORIZED_CREATE_USER.getCode())
                .errorMessage(ErrorCode.ERR_UNAUTHORIZED_CREATE_USER.getMessage().formatted(user))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message, error);
    }

    public IcesiError exceptionMethodArgumentValid(String s, String message){
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_METHOD_ARGUMENT_NOT_VALID.getCode())
                .errorMessage(ErrorCode.ERR_METHOD_ARGUMENT_NOT_VALID.getMessage().formatted(message))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return error;
    }
}
