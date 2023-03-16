package com.example.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;
    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;

    public IcesiRole() {}
}
