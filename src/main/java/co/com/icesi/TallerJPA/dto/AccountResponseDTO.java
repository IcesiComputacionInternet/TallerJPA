package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class AccountResponseDTO {
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    private UserResponseDTO user;

}
