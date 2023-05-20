package co.com.icesi.icesiAccountSystem.error.util;

import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemError;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemErrorDetail;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemException;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.function.Supplier;

public class AccountSystemExceptionBuilder {
    public static Supplier<AccountSystemException> createAccountSystemException(String message, DetailBuilder... details) {
        return () -> new AccountSystemException(message, createAccountSystemError(message, HttpStatus.BAD_REQUEST, details));
    }

    public static Supplier<AccountSystemException> createAccountSystemException(String message, HttpStatus httpStatus,DetailBuilder... details) {
        return () -> new AccountSystemException(message, createAccountSystemError(message, httpStatus, details));
    }

    public static AccountSystemError createAccountSystemError(String message, HttpStatus httpStatus, DetailBuilder... details){
        return AccountSystemError.builder().status(httpStatus)
                .details(
                        Arrays.stream(details)
                                .map(AccountSystemExceptionBuilder::mapToAccountSystemErrorDetail)
                                .toList()
                ).build();
    }

    public static AccountSystemErrorDetail mapToAccountSystemErrorDetail(DetailBuilder detailBuilder) {
        return AccountSystemErrorDetail.builder()
                .errorCode(detailBuilder.getErrorCode().getCode())
                .errorMessage(detailBuilder.getErrorCode().getMessage().formatted(detailBuilder.getFields()))
                .build();

    }
}
