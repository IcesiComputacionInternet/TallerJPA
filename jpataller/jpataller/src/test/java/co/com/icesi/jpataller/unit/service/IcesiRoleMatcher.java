package co.com.icesi.jpataller.unit.service;

import co.com.icesi.jpataller.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    public IcesiRoleMatcher(IcesiRole icesiRoleLeft){
        this.icesiRoleLeft = icesiRoleLeft;
    }

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId()!= null &&
                Objects.equals(icesiRoleRight.getName(), icesiRoleLeft.getName()) &&
                Objects.equals(icesiRoleRight.getDescription(), icesiRoleLeft.getDescription());
    }
}
