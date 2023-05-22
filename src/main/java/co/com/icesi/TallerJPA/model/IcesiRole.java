package co.com.icesi.TallerJPA.model;


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
public class IcesiRole {
    @Id
    private UUID roleId;

    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;
}
