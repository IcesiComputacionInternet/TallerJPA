package co.com.icesi.demojpa.dto.response;

import co.com.icesi.demojpa.validate.accountNum.AccountNumber;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class ResponseAccountDTO {

    @AccountNumber
    private String accountNumber;

    @Min(value = 0,message = "a")
    @Max(value = 100, message = "aaaa")
    private long balance;

    private String type;

    private boolean active;

    private ResponseUserDTO responseUserDTO;
}
