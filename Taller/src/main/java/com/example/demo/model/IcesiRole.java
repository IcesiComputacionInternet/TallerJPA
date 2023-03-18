package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
public class IcesiRole {

    @Id
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> users;
}
