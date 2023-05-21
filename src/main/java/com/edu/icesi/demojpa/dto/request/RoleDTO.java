package com.edu.icesi.demojpa.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {
    private String description;
    private String name;
}
