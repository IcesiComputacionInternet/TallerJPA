package com.example.demo.DTO;

import com.example.demo.model.TypeIcesiAccount;

public class ResponseIcesiAccountDTO {
    private String accountNumber;
    private long balance;
    private TypeIcesiAccount type;
    private boolean active;
    private ResponseIcesiUserDTO user;
}
