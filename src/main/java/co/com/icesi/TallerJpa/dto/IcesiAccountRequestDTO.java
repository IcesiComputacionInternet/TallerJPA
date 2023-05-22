package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountRequestDTO {
    @Min(value = 0, message = "must be minimum 0.")
    private Long balance;

    @NotNull(message = "of the account can't be null.")
    @Enumerated()
    private AccountType type;

    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    @Email(message = "is not a valid email.")
    private String userEmail;
}
