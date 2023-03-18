package com.edu.icesi.TallerJPA.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "icesiUser")
    private List<IcesiAccount> accounts;

    @ManyToOne(optional = false)
    @JoinColumn(name = "icesi_role_role_id", nullable = false)
    private IcesiRole icesiRole;
}
