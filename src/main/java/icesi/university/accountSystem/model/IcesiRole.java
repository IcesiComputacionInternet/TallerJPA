package icesi.university.accountSystem.model;

import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
@Data
@Entity
@Builder
@AllArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> icesiUsers;
}
