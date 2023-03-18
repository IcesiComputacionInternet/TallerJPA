package co.edu.icesi.tallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private long balance;
    private String type;
    private boolean active;
    private IcesiUserCreateDTO icesiUserDTO;
}
