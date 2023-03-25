package co.edu.icesi.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {

    private String accountNumberFrom;

    private String accountNumberTo;

    private long money;

    private String result;


}
