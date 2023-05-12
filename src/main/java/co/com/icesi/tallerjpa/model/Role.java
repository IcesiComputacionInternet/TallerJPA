package co.com.icesi.tallerjpa.model;

import co.com.icesi.tallerjpa.model.security.UserPermission;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private UUID roleId;
    private String name;
    private String description;
    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_permission_roles",
            joinColumns = @JoinColumn(name = "role_role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_permission_permission_id")
    )
    private List<UserPermission> permissionList;

}
