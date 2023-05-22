package co.com.icesi.TallerJpa.unit.matcher;

import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {
    private IcesiRole icesiRoleLeft;
    @Override
    public boolean matches(IcesiRole icesiRoleRight){
        return icesiRoleRight.getRoleId() != null &&
                Objects.equals(icesiRoleRight.getName(),icesiRoleLeft.getName()) &&
                Objects.equals(icesiRoleRight.getDescription(),icesiRoleLeft.getDescription());
    }
}
