package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class IcesiRoleDto {

    private UUID roleId;

    private String description;

    private String name;
}
