package com.example.demo.DTO;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IcesiAccountDTO {
    private UUID accountId;
    private String accountNumber;
    private String userEmail;
    private Long balance;
    private String type;
    private boolean active;
}
