package co.com.icesi.TallerJpa.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;
    private String name;
    private String description;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> icesiUsers;
}
