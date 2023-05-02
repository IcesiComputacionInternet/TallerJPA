package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {
    private IcesiRole icesiUserLeft;

    public IcesiRoleMatcher(IcesiRole icesiRoleLeft){
        this.icesiRoleLeft = icesiRoleLeft;
    }
    private IcesiRole icesiRoleLeft;

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId() != null &&
                Objects.equals(icesiRoleRight.getDescription(), icesiRoleLeft.getDescription()) &&
                Objects.equals(icesiRoleRight.getName(), icesiRoleLeft.getName());
    }
}
