package com.example.tallerjpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
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

    @JsonIgnore
    @OneToMany(mappedBy = "icesiUser")
    private List<IcesiAccount> accountList;

    @ManyToOne
    @JoinColumn(name = "icesi_role_id")
    private IcesiRole icesiRole;



}
