package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Type;
@Data
@Builder
public class RequestAccountDTO {
    @Min(value=0)
    @Max(value = 100)
    private Long balance;

    private TypeAccount type;
    @NotBlank
    private String user;
}
