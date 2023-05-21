package com.example.demo.DTO;


import com.example.demo.model.IcesiUser;
import com.example.demo.model.enums.TypeIcesiAccount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private String accountNumber;

    private long balance;

    private TypeIcesiAccount type;

    private boolean active;

    private IcesiUser icesiUser;

}
