package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAccountDTO {
    private Long balance;
    private TypeAccount type;
    private String user;
}
