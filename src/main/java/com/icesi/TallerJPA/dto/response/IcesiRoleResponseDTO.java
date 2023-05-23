package com.icesi.TallerJPA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRoleResponseDTO {

    private UUID roleId;
    private String description;
    private String name;

}
