package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountUpdateDTO {
    private String accountNumber;

    private String type;

    private boolean active;

}
