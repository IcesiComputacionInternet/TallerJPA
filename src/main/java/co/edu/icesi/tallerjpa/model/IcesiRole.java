package co.edu.icesi.tallerjpa.model;

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
    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> icesiUsers;
}
