package com.icesi.TallerJPA.error.util;

import com.icesi.TallerJPA.error.exception.DetailBuilder;
import com.icesi.TallerJPA.error.exception.IcesiError;
import com.icesi.TallerJPA.error.exception.IcesiErrorDetail;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class IcesiExceptionBuilder {

    public static IcesiError createIcesiError(HttpStatus httpStatus, DetailBuilder... details){
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
}
