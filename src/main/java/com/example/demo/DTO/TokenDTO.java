package com.example.demo.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
public class TokenDTO {
    private String token;
}
