package co.edu.icesi.tallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class IcesiAccountToShowDTO {
    private UUID id;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    private IcesiUserCreateDTO icesiUserDTO;
}
