package com.edu.icesi.demojpa.model;

import lombok.*;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;
}
