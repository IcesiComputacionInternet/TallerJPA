package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRoleDTO {
    private UUID roleId;
    private String description;
    private String name;

    private List<IcesiUser> icesiUsers;
}
