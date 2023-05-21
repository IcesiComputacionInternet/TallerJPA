package com.edu.icesi.TallerJPA.unit.matcher;

import com.edu.icesi.TallerJPA.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    public IcesiRoleMatcher(IcesiRole icesiRoleLeft){
        this.icesiRoleLeft = icesiRoleLeft;
    }

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId() != null &&
                Objects.equals(icesiRoleLeft.getName(), icesiRoleRight.getName()) &&
                Objects.equals(icesiRoleLeft.getDescription(), icesiRoleRight.getDescription());
    }
}
