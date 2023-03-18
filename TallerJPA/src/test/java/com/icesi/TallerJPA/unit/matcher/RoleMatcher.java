package com.icesi.TallerJPA.unit.matcher;

import com.icesi.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class RoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleRight.getRoleId() != null &&
                icesiRoleRight.getName().equals(icesiRoleLeft.getName()) &&
                icesiRoleRight.getDescription().equals(icesiRoleLeft.getDescription());
    }
}
