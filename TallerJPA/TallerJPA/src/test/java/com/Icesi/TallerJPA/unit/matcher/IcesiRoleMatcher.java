package com.Icesi.TallerJPA.unit.matcher;

import com.Icesi.TallerJPA.model.IcesiRole;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRole;

    public IcesiRoleMatcher(IcesiRole icesiRole){
        this.icesiRole = icesiRole;
    }

    @Override
    public boolean matches(IcesiRole icesiRoleOne) {
        return icesiRoleOne.getRoleId() != null &&
                Objects.equals(icesiRole.getName(), icesiRoleOne.getName()) &&
                Objects.equals(icesiRole.getDescription(), icesiRoleOne.getDescription());
    }
}
