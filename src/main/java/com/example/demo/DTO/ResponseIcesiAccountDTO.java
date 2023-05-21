package com.example.demo.DTO;

import com.example.demo.model.enums.TypeIcesiAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseIcesiAccountDTO {
    private String accountNumber;
    private long balance;
    private TypeIcesiAccount type;
    private boolean active;
    private ResponseIcesiUserDTO responseIcesiUserDTO;
}
