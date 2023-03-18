package com.edu.icesi.demojpa.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
public class IcesiUser {

    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<IcesiAccount> accounts;

    @ManyToOne(optional = false)
    @JoinColumn(name = "icesi_role_users", nullable = false)
    private IcesiRole role;
}
