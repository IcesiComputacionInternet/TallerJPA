package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDto {
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;
    private ResponseUserDto user;
}