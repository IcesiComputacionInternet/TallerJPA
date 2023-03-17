package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ICESIUSER")
public class IcesiUser {
    @Id
    private UUID userId;

    private String firstName;

    private String lastname;

    private String email;

    private String phone;

    private String password;






    @ManyToOne
    @JoinColumn(name="icesi_role_role_id", nullable=false)
    private IcesiRole role;
    //TODO desdocumentar esto

    @OneToMany(mappedBy = "account")
    private List<IcesiAccount> accounts;


/*
    @OneToMany(mappedBy = "user")
    private List<IcesiBlogPost> post;

     */

}
