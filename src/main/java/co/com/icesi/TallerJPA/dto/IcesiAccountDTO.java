package co.com.icesi.TallerJPA.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDTO {

    @Min(value = 0, message = "The balance must be greater than 0")
    private Long balance;

    @NotBlank
    private String type;

    private IcesiUserDTO user;


}
