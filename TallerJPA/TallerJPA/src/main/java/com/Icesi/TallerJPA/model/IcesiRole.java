package com.Icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
public class IcesiRole {
    @Id
    private UUID roleId;
    private String description;
    private String name;

   @OneToMany(mappedBy = "icesiRole")
   private List<IcesiUser> icesiUsers;
    @PrePersist
    public void generateId() {
        this.roleId = UUID.randomUUID();
    }
}
