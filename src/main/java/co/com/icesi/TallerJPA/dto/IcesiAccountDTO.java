package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDTO {
    private Long balance;

    private String type;

    private IcesiUserDTO user;


}
