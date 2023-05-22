package co.com.icesi.demojpa.dto.request;

import co.com.icesi.demojpa.enums.IcesiAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {

    @Min(value = 0)
    @Max(value = 100)
    private Long balance;
    private IcesiAccountType type;
    private String userId;
    private String email;

}
