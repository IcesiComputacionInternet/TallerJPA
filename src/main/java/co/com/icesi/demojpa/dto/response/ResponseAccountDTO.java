package co.com.icesi.demojpa.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDTO {

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private ResponseUserDTO responseUserDTO;
}
