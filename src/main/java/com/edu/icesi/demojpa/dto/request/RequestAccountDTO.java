package com.edu.icesi.demojpa.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RequestAccountDTO {
    private long balance;
    private String type;
    private boolean active;
    private UUID icesiUserId;
    private String accountNumber;
}
