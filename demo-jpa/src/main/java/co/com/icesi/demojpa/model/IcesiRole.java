package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {
    @Id
    private UUID roleId;

    @Column(unique = true)
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<IcesiUser> users;

}
