package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RequestAccountDTO {
    @NotNull(message = "The email of a user can't be null")
    @NotBlank(message = "The email of a user can't be blank")
    private String userEmail;

    @Min(value=0, message = "The balance of a new account must be greater than 0")
    private long balance;

    @NotNull(message = "The type of an account can't be null")
    @NotBlank(message = "The type of an account can't be blank")
    private String type;
}
