package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequestDTO {

    private Long balance;

    private String type;

    private UserDTO user;
}
