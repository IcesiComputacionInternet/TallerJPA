package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
public class IcesiUser {
    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> accounts;

    @ManyToOne
    private IcesiRole role;
}
