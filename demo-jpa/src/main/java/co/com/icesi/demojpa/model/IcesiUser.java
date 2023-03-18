package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
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
    @JoinColumn(name = "role_id", nullable = false)
    private IcesiRole role;
}
