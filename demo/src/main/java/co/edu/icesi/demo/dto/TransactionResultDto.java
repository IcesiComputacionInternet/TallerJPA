package co.edu.icesi.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResultDto {
    private String accountFrom;
    private String accountTo;
    private Long amount;
    private String result;
}
