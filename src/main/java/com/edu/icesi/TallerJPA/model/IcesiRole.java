package com.edu.icesi.TallerJPA.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {

    @Id
    private UUID roleId;

    private String description;

    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> icesiUsers;
}
