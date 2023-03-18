package co.com.icesi.TallerJpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> icesiUsers;
}
