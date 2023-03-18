package co.com.icesi.TallerJPA.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class IcesiRole {
    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;
    @Id
    private UUID roleId;
    private String description;
    private String name;
}
