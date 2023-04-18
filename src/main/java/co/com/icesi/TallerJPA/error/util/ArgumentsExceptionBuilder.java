package co.com.icesi.TallerJPA.error.util;

import co.com.icesi.TallerJPA.error.exception.ArgumentsError;
import co.com.icesi.TallerJPA.error.exception.ArgumentsErrorDetail;
import co.com.icesi.TallerJPA.error.exception.ArgumentsException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.function.Supplier;

public class ArgumentsExceptionBuilder {

    public static Supplier<ArgumentsException> createArgumentsExceptionSup(String message, HttpStatus httpStatus,DetailBuilder... details){
        return () -> new ArgumentsException(message,createArgumentsError(message, HttpStatus.BAD_REQUEST,details));

    }

    public static ArgumentsException createArgumentsException(String message, HttpStatus httpStatus,DetailBuilder... details) {
        return new ArgumentsException(message, createArgumentsError(message, httpStatus, details));
    }

    public static ArgumentsError createArgumentsError(String message,HttpStatus httpStatus,DetailBuilder... details){
        return ArgumentsError.builder()
                .status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(ArgumentsExceptionBuilder::mapToArgumentsErrorDetail)
                                .toList()
                ).build();
    }

    public static ArgumentsErrorDetail mapToArgumentsErrorDetail(DetailBuilder detailBuilder){
        return ArgumentsErrorDetail.builder()
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();
    }
}
