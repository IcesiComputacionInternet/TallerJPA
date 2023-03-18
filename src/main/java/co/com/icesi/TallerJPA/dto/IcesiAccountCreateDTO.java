package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private Long balance;
    private String type;
    private boolean active;
    //La inclusión de IcesiUserCreateDTO icesiUser en IcesiAccountCreateDTO permite asociar un IcesiUserCreateDTO con un IcesiAccountCreateDTO.
    private IcesiUserCreateDTO icesiUser;
}
