package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateIcesiAccountDTO {
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    private CreateIcesiUserDTO icesiUserDTO;
}
