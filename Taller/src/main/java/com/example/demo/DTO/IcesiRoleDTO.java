package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
public class IcesiRoleDTO {

    private UUID roleId;
    private String description;
    private String name;
}
