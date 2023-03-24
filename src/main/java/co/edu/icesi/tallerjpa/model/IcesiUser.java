package co.edu.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "The icesi role can not be null")
    @ManyToOne
    @JoinColumn(name = "icesiRole_roleId", nullable = false)
    private IcesiRole icesiRole;
    @OneToMany(mappedBy = "icesiUser")
    private List<IcesiAccount> icesiAccounts;
}
