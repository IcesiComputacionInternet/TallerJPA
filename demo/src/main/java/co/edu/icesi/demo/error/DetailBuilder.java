package co.edu.icesi.demo.error;

import lombok.Getter;

import lombok.Builder;

@Getter
@Builder
public class DetailBuilder {

    private final ErrorCode errorCode;
    private final Object[] fields;

    public DetailBuilder(ErrorCode errorCode, Object... fields){
        this.errorCode = errorCode;
        this.fields = fields;
    }

}