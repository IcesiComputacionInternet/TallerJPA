package com.example.tallerjpa.dto;


import com.example.tallerjpa.model.IcesiUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private long balance;
    private String type;
    private boolean active;
    private String emailUser;

}
