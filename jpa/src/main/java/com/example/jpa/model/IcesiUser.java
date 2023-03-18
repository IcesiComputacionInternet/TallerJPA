package com.example.jpa.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;
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

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> icesiAccountList;

    @ManyToOne(optional = false) //The user role can't be null.
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;

}
