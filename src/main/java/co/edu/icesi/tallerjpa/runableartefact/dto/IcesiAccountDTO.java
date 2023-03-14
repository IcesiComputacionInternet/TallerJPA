package co.edu.icesi.tallerjpa.runableartefact.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountDTO {

    private Long balance;

    private String type;

    private boolean active;

    private String icesiUserEmail;
}
