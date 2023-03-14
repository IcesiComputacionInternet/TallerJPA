package co.com.icesi.demojpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="ICESIROLE")
public class IcesiRole {

    @Id
    private UUID roleId;

    private String name;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> user;

}