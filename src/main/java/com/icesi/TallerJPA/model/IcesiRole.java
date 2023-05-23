package com.icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRole {

    @Id
    private UUID roleId;
    private String description;
    private String name;

    @OneToMany(mappedBy = "icesiRole")
    private List<IcesiUser> icesiUserList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "icesi_role_permission",
            joinColumns = @JoinColumn(name = "icesi_role_id"),
            inverseJoinColumns = @JoinColumn(name = "icesi_permission_id")
    )
    private List<IcesiPermits> permissionList;

    @Override
    public String toString() {
        return "IcesiRole{" +
                "roleId=" + roleId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
