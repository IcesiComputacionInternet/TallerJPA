package co.com.icesi.jpataller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IcesiUser {

    @Id
    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    @ManyToOne
    @JoinColumn(name="icesi_role_role_id", nullable = false)
    private IcesiRole role;

    @OneToMany(mappedBy = "accountOwner")
    private List<IcesiAccount> accounts;
}
