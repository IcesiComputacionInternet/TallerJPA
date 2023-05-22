package com.Icesi.TallerJPA.model;

import com.Icesi.TallerJPA.validation.CustomAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Table
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
    @CustomAnnotations.PasswordValidation
    private String password;

    @OneToMany
    private List<IcesiAccount> accounts;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private IcesiRole icesiRole;
}
