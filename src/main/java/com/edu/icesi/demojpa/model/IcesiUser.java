package com.edu.icesi.demojpa.model;

import com.sun.istack.NotNull;
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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "icesi_role_users", nullable = false)
    private IcesiRole role;
}
