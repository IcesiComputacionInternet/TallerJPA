package com.edu.icesi.TallerJPA.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
public class IcesiRole {

    @Id
    private UUID roleId;

    private String description;

    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private Set<IcesiUser> icesiUsers;
}
