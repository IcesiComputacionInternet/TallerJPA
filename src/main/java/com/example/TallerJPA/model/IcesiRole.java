package com.example.TallerJPA.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;
}
