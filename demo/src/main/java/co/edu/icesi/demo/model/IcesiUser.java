package co.edu.icesi.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
public class IcesiUser {

    @Id
    private UUID userId;

    private String firstname;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;
    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> icesiAccounts;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private IcesiRole role;
}
