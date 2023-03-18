package com.edu.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateDTO {
    private long balance;
    private String type;
    private boolean active;
    private String icesiUserId;
}
