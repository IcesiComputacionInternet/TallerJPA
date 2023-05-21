package com.example.tallerjpa.dto;


import com.example.tallerjpa.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
