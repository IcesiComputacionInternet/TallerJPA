package com.example.demo.DTO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRoleDTO {

    private UUID roleId;
    private String description;
    private String name;
}
