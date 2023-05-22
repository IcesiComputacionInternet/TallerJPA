package com.example.tallerjpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRole {

    @Id
    private UUID roleId;
    private String description;
    private String name;


    @OneToMany (mappedBy = "icesiRole")
    private List<IcesiUser> icesiUserList;
}
