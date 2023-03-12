package com.example.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateDTO {
    private String userEmail;
    private String type;
}
