package co.edu.icesi.tallerjpa.runableartefact.model;

import co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.interfaces.ColombianNumber;
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

    @ColombianNumber
    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "accountId")
    private List<IcesiAccount> accounts;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;
}
