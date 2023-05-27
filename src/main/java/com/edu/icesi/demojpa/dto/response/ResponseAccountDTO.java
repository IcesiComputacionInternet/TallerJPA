package com.edu.icesi.demojpa.dto.response;

import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.model.IcesiUser;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ResponseAccountDTO {
    private String result;
    private long balance;
    private String type;
    private boolean active;
    private String accountNumber;
    private UUID userId;
}
