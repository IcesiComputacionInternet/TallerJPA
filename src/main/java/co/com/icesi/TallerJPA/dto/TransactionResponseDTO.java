package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {
    private String result;
    private String oldStateOfTheAccount;
    private String newStateOfTheAccount;

}
