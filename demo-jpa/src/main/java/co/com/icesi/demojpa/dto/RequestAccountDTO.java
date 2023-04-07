package co.com.icesi.demojpa.dto;

import co.com.icesi.demojpa.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RequestAccountDTO {

    @Min (value = 0, message = "")
    @Max( value = 100)
    private Long balance;

    private TypeAccount type;

    @NotBlank
    private String user;

}
