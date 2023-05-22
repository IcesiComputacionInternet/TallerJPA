package co.com.icesi.TallerJPA.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDTO {

    private Long balance;
    @NotBlank
    private String type;

    private IcesiUserDTO user;


}
