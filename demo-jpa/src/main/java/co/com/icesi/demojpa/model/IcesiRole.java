package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;

}
