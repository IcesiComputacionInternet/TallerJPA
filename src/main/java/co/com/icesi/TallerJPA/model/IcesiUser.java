package co.com.icesi.TallerJPA.model;

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

    //Un usuario tiene multiples cuentas
    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> accounts;

    //Los usuarios tienen un rol
    @ManyToOne
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;



}
