package co.com.icesi.tallerjpa.error.util;

import co.com.icesi.tallerjpa.error.exception.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class IcesiExceptionBuilder {
    public static IcesiError createIcesiError(String message, HttpStatus httpStatus, DetailBuilder... details){
        return IcesiError.builder()
                .status(httpStatus)
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

    public IcesiException notFoundException (String message, String field1, String field2, String field3) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder().errorCode(ErrorCode.ERR_404.getCode())
                .errorMessage(ErrorCode.ERR_404.getMessage().formatted(field1, field2, field3))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message,error);
    }

    public IcesiException permissionException (String message) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder().errorCode(ErrorCode.ERR_401.getCode())
                .errorMessage(ErrorCode.ERR_401.getMessage())
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message,error);
    }

    public IcesiException forbiddenException (String message) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder().errorCode(ErrorCode.ERR_403.getCode())
                .errorMessage(ErrorCode.ERR_403.getMessage())
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message,error);
    }

    public IcesiException generalException (String message, String field1) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder().errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(ErrorCode.ERR_400.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message,error);
    }

    public IcesiException duplicatedException (String message, String field1) {
        IcesiErrorDetail errorDetail = IcesiErrorDetail.builder().errorCode(ErrorCode.ERR_DUPLICATED.getCode())
                .errorMessage(ErrorCode.ERR_DUPLICATED.getMessage().formatted(field1))
                .build();

        IcesiError error = IcesiError.builder()
                .status(HttpStatus.CONFLICT)
                .details(List.of(errorDetail))
                .build();

        return new IcesiException(message,error);
    }

}