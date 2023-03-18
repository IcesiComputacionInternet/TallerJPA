package co.com.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {

    @OneToMany(mappedBy="role")
    private List<IcesiUser> user;

    @Id
    private UUID roleId;
    private String description;
    private String name;
}
