package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {
    private IcesiRole icesiRoleLeft;
    public IcesiRoleMatcher(IcesiRole icesiRoleLeft){ this.icesiRoleLeft = icesiRoleLeft;}

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId() != null &&
                Objects.equals(icesiRoleRight.getDescription(), icesiRoleLeft.getDescription()) &&
                Objects.equals(icesiRoleRight.getName(), icesiRoleLeft.getName());
    }

}
