package co.edu.icesi.demo.model;

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

    @Column(unique=true)
    private String email;

    @Column(unique=true)
    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> accounts;

    @ManyToOne
    @JoinColumn(name="icesi_role_role_id", nullable = false)
    private IcesiRole role;


}
