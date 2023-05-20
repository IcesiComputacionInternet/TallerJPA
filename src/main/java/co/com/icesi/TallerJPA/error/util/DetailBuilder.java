package co.com.icesi.TallerJPA.error.util;

import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import lombok.Getter;


@Getter
public class DetailBuilder {

    private final ErrorCode errorCode;
    private final Object[] fields;

    public DetailBuilder(ErrorCode errorCode, Object... fields){ //... zero or more Objects may be passed
        this.errorCode = errorCode;
        this.fields = fields;
    }

}
