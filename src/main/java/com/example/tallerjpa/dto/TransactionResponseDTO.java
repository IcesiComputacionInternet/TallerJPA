package com.example.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {
    private String result;
    private String oldBalance;
    private String newBalance;

}
