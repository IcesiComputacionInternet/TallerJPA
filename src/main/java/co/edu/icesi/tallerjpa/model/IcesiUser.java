package co.edu.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
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

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;

    @OneToMany(mappedBy = "accountId")
    private List<IcesiAccount> accounts;

}
