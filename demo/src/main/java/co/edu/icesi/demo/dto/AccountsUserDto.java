package co.edu.icesi.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AccountsUserDto {
    private List<IcesiAccountDto> userAccounts;
}
