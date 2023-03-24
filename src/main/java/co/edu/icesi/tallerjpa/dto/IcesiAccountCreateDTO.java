package co.edu.icesi.tallerjpa.dto;

import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private long balance;
    private TypeIcesiAccount type;
    private boolean active;
    private IcesiUserCreateDTO icesiUserDTO;
}
