package co.com.icesi.tallerjpa.error.util;

import co.com.icesi.tallerjpa.error.exception.CustomError;
import co.com.icesi.tallerjpa.error.exception.CustomErrorDetail;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ExceptionBuilder {

    public static CustomException createCustomException(String message, DetailBuilder... details) {
        return new CustomException(message, createCustomError(message, HttpStatus.BAD_REQUEST, details));
    }

    public static CustomException createCustomException(String message, HttpStatus httpStatus,DetailBuilder... details) {
        return new CustomException(message, createCustomError(message, httpStatus, details));
    }

    public static CustomError createCustomError(String message, HttpStatus httpStatus, DetailBuilder... details){
        return CustomError.builder().status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(ExceptionBuilder::mapToCustomErrorDetail)
                                .toList()
                ).build();
    }

    public static CustomErrorDetail mapToCustomErrorDetail(DetailBuilder detailBuilder) {
        return CustomErrorDetail.builder()
                .errorCode(detailBuilder.errorCode().getCode())
                .errorMessage(detailBuilder.errorCode().getMessage().formatted(detailBuilder.fields()))
                .build();

    }

}
