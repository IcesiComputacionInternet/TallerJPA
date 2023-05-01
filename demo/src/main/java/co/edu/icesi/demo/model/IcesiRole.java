package co.edu.icesi.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;

    private String roleDescription;

    private String name;

    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> icesiUsers;
}
