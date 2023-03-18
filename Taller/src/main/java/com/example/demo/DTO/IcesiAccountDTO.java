package com.example.demo.DTO;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiAccountDTO {
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;
}
