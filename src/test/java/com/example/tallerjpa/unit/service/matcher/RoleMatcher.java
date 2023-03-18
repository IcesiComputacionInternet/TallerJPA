package com.example.tallerjpa.unit.service.matcher;

import com.example.tallerjpa.model.IcesiRole;
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
