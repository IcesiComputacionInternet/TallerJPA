package com.example.TallerJPA.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
public class IcesiUser {
    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> accounts;
    @ManyToOne
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;
}
