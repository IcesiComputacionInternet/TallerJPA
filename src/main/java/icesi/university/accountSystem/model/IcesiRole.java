package icesi.university.accountSystem.model;

import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> icesiUsers;
}
