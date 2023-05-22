package com.example.demo.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRole {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "pg-uuid")
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> users;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "icesi_permission_roles",
            joinColumns = @JoinColumn(name = "icesi_role_role_id"),
            inverseJoinColumns = @JoinColumn(name = "icesi_permission_permission_id")
    )
    private List<IcesiPermission> permissionList;
}
