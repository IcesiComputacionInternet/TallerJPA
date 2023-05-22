package icesi.university.accountSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUser {

    @Id
    private UUID userId;
    private String firstName;
    private String lastName;

    private String email;

    private  String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<IcesiAccount> accounts;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id",nullable = false)
    private IcesiRole role;

}
