package com.icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiPermits {

    @Id
    private UUID permitId;
    private String path;
    private String keyIdentifier;

    @ManyToMany(mappedBy = "permissionList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IcesiRole> roles;
}
