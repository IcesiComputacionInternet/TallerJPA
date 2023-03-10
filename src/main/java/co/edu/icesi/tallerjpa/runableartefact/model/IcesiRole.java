package co.edu.icesi.tallerjpa.runableartefact.model;

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
    private String name;

    private String description;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> icesiUsers;
}
