package co.edu.icesi.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class IcesiRole {

    @Id
    private UUID roleId;

    private String description;

    private String name;
}
