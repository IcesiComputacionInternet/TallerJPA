package com.example.demo.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class IcesiRole {
    @Id
    private UUID roleId;

    private String description;

    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> users;
}
