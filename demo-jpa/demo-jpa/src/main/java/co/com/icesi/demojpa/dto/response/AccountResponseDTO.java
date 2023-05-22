package co.com.icesi.demojpa.dto.response;

import co.com.icesi.demojpa.enums.IcesiAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private Long balance;
    private IcesiAccountType type;
    private boolean active;
    private UserResponseDTO user;
}