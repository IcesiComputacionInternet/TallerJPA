package co.com.icesi.icesiAccountSystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
@Data
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
