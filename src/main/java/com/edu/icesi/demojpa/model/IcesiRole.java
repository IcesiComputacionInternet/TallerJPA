package com.edu.icesi.demojpa.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "role")
    private Set<IcesiUser> users;

}
