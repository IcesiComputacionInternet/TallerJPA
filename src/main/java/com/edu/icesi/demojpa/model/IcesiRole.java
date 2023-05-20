package com.edu.icesi.demojpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {

    @Id
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<IcesiUser> users;

}
