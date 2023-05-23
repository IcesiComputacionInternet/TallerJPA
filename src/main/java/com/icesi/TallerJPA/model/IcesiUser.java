package com.icesi.TallerJPA.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonIgnoreProperties("icesiUserList")
    @ManyToOne
    @JoinColumn(name = "icesi_rol_id")
    private IcesiRole icesiRole;
}
