package com.edu.icesi.demojpa.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import java.util.UUID;

@Getter
@Setter
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
