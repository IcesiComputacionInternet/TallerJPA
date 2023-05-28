package co.com.icesi.icesiAccountSystem.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> listOfUsers;
    @Id
    private UUID roleId;
    private String description;
    private String name;
}
