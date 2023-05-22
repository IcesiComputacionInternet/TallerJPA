package co.edu.icesi.tallerjpa.runableartefact.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInformationResponseDTO {

    private String accountNumberOrigin;

    private String accountNumberDestination;

    private String message;

    private long amount;
}
