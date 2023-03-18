package com.example.demo.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import lombok.Builder;
import lombok.Data;

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
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole icesiRole;
}
