package com.example.tallerjpa.dto;


import com.example.tallerjpa.model.AccountType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AccountDTO {


    private long balance;
    private AccountType type;
    private boolean active;


    private String emailUser;


}
