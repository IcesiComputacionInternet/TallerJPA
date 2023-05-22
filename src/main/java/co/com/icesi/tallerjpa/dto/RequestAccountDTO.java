package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccountDTO {
    @Min(value = 0, message = "The balance must be greater than 0")
    private Long balance;
    private TypeAccount type;
    private String user;
}
