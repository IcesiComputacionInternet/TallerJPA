package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequestDTO {

    private long balance;

    private String type;

    private UserRequestDTO user;
}
