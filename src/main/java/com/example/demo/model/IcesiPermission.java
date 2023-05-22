package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class IcesiPermission {

    @Id
    @GeneratedValue
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "pg-uuid")
    private UUID permissionId;
    private String path;
    @Column(name = "`key`")
    private String key;

    @ManyToMany(mappedBy = "permissionList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IcesiRole> roles;

}