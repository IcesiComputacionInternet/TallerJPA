package co.com.icesi.demojpa.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class AccountCreateDTO {
    private UUID accountId;
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @Min(value = 0, message = "Balance must be greater than or equal to zero")
    private long balance;
    @NotBlank(message = "Type is required")
    private String type;
    private boolean active;
    @NotNull(message = "User is required")
    private UserCreateDTO user;
}
