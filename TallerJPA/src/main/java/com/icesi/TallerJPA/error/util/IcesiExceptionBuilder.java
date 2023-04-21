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

    public void throwExceptionDuplicated(String message, String field1, String field2, String field3) {
        throw new IcesiException(message, IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(
                        IcesiErrorDetail.builder()
                                .errorCode(ErrorCode.ERR_DUPLICATED.getCode())
                                .errorMessage(ErrorCode.ERR_DUPLICATED.getMessage().formatted(field1, field2, field3))
                                .build()
                ))
                .build());
    }
}
