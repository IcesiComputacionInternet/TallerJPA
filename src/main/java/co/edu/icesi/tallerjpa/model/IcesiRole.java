package co.edu.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Id
    private UUID roleId;
    private String name;
    private String description;
    @OneToMany(mappedBy = "role")
    private List<IcesiUser> icesiUsers;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IcesiUser> getUsers() {
        return icesiUsers;
    }

    public void setIcesiUsers(List<IcesiUser> icesiUsers) {
        this.icesiUsers = icesiUsers;
    }

    public String getDescription() {
        return description;
    }
}
