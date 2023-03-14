package co.edu.icesi.tallerjpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class IcesiUser {
    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    @ManyToOne
    @JoinColumn(name = "IcesiRole_roleId", nullable = false)
    private IcesiRole icesiRole;
    @OneToMany(mappedBy = "icesiUser")
    private List<IcesiAccount> icesiAccounts;
}
