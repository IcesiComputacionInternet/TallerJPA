package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class RoleMatcher  implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    public RoleMatcher(IcesiRole icesiRoleLeft){

        this.icesiRoleLeft=icesiRoleLeft;
    }
    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId()!=null &&
                Objects.equals(icesiRoleRight.getName(),icesiRoleLeft.getName()) &&
                Objects.equals(icesiRoleRight.getDescription(),icesiRoleLeft.getDescription());
    }
}
