package com.example.demo.error.util;


import org.springframework.http.HttpStatus;

import com.example.demo.error.exception.DetailBuilder;
import com.example.demo.error.exception.IcesiError;
import com.example.demo.error.exception.IcesiErrorDetail;
import com.example.demo.error.exception.IcesiException;

import java.util.Arrays;
import java.util.function.Supplier;

public class IcesiExceptionBuilder {
    public static IcesiError createIcesiError(String message, HttpStatus httpStatus, DetailBuilder... details){
        return IcesiError.builder().status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(IcesiExceptionBuilder::mapToIcesiErrorDetail)
                                .toList()
                ).build();
    }

    public static IcesiErrorDetail mapToIcesiErrorDetail(DetailBuilder detailBuilder) {
        return IcesiErrorDetail.builder()
                .errorCode(detailBuilder.errorCode().getCode())
                .errorMessage(detailBuilder.errorCode().getMessage().formatted(detailBuilder.fields()))
                .build();
    }

    public static Supplier<IcesiException> createIcesiException(String message, HttpStatus httpStatus, DetailBuilder... details) {
        return () -> new IcesiException(message, createIcesiError(message, httpStatus, details));
    }

}
