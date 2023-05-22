package com.edu.icesi.demojpa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccountDTO {
    @Min(value = 0, message = "The balance can't be below 0")
    private long balance;
    private String type;
    private boolean active;
    private UUID icesiUserId;
    private String accountNumber;
}
