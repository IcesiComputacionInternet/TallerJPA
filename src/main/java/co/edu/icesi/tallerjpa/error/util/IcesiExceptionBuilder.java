package co.edu.icesi.tallerjpa.error.util;

import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiErrorDetail;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

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
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();
    }

}
