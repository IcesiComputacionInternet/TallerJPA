package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiRoleCreateDTO {
    private String description;

    private String name;
}
