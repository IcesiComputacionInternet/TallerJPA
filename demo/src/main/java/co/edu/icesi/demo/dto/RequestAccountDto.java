package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAccountDto {
    private Long balance;
    private TypeAccount type;
    private String user;
}
