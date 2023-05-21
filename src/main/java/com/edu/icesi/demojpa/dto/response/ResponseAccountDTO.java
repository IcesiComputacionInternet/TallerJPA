package com.edu.icesi.demojpa.dto.response;

import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDTO {
    private String result;
    private long balance;
    private String type;
    private boolean active;
    private String accountNumber;
    private RequestUserDTO icesiUser;
}
