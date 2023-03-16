package com.edu.icesi.demojpa.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class IcesiUser {

    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    @OneToMany(mappedBy = "icesiUser")
    private Set<IcesiAccount> icesiAccounts;

    @ManyToOne(optional = false)
    @JoinColumn(name = "icesi_user_user_id", nullable = false)
    private IcesiRole icesiRole;
}
