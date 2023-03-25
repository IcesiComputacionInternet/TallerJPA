package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountDTOResponse {

    private String accountNumber;
    private Long balance;

    private String type;

    private IcesiUserDTO user;

}
