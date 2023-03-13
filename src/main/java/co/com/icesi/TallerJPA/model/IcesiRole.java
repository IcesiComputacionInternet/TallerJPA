package co.com.icesi.TallerJPA.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.Entity;

import javax.persistence.FetchType;
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
    private String description;
    private String name;

    //Un rol puede pertenecer a varios usuarios
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<IcesiUser> users;


}
