package co.edu.icesi.tallerJPA.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }), @UniqueConstraint(columnNames = { "phoneNumber" }) })
public class IcesiUser {
    @Id
    private UUID userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    private List<IcesiAccount> accounts;
    @ManyToOne
    @NonNull
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole role;
}
