package co.edu.icesi.tallerjpa.dto;

import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@Builder
public class IcesiAccountCreateDTO {
    @Min(value = 0, message = "The min value for the balance is 0")
    private long balance;
    @NotNull(message = "The icesi account type can not be null")
    private TypeIcesiAccount type;
    private boolean active;
    @NotNull(message = "The email can not be null")
    @NotBlank(message = "The email can not be blank")
    @NotEmpty(message = "The email can not be empty")
    @Email
    private String icesiUserEmail;
}
