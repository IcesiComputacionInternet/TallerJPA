package co.com.icesi.TallerJPA.error.util;

import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.error.exception.ErrorDetail;

public class ErrorManager {
    public static ErrorDetail createDetail(String message, ErrorCode errorCode){
        return ErrorDetail.builder().errorCode(errorCode).errorMessage(message).build();
    }

    public static ErrorDetail[] sendDetails(ErrorDetail ... details){
        return details;
    }
}
