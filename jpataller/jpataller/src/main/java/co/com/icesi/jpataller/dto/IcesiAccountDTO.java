package co.com.icesi.jpataller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountDTO {
    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private String userId;
}
