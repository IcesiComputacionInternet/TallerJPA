package co.edu.icesi.demo.unit.matcher;

import co.edu.icesi.demo.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {
    private IcesiRole icesiRoleLeft;

    public IcesiRoleMatcher(IcesiRole icesiRoleLeft){
        this.icesiRoleLeft = icesiRoleLeft;
    }

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId()!=null &&
                Objects.equals(icesiRoleRight.getRoleName(),icesiRoleLeft.getRoleName()) &&
                Objects.equals(icesiRoleRight.getRoleDescription(),icesiRoleLeft.getRoleDescription());
    }
}
