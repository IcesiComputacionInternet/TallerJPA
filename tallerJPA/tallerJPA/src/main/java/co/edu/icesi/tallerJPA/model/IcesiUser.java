package co.edu.icesi.tallerJPA.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
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
    @OneToMany(mappedBy = "user")
    private List<Account> accounts;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_role_id")
    private Role role;
}
