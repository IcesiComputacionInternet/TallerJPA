package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleCreateDTO {
    private String description;
    private String name;
}
