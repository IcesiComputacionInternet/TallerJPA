package co.com.icesi.demojpa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class TransactionResultDTO {

    private String result;
    private String accountFrom;
    private String accountTo;
    private String amount;
}
