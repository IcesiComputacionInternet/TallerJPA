package co.com.icesi.TallerJPA.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;

    //Un rol puede pertenecer a varios usuarios
    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;


}
