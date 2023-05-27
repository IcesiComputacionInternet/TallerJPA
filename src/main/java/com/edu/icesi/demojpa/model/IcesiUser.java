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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "icesi_role_role_id", nullable = false)
    private IcesiRole role;
}
