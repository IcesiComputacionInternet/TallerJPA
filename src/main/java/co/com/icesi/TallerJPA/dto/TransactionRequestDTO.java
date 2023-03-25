package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequestDTO {
   private String accountNumberFrom;
    private String accountNumberTo;
    private Long amount;



}
