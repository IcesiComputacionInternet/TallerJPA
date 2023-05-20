package co.com.icesi.icesiAccountSystem.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUser {

    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> listOfAccounts;


    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="icesi_role_role_id", nullable=false)
    private IcesiRole role;

    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String phoneNumber;
    private String password;
}
