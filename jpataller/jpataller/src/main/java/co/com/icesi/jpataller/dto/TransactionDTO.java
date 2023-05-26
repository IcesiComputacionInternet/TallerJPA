package co.com.icesi.jpataller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String accountNumberOrigin;

    private String accountNumberDestiny;

    private long amount;

    private String resultMessage;
}
