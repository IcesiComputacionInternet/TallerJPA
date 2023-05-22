package com.example.tallerjpa.dto;


import com.example.tallerjpa.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO{

    @Min(value = 0)
    private long balance;
    private AccountType type;
    private boolean active;
    private String emailUser;


}
