package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        return icesiRoleLeft.getRoleId() != null &&
                Objects.equals(icesiRoleLeft.getName(), icesiRoleRight.getName()) &&
                Objects.equals(icesiRoleLeft.getDescription(), icesiRoleRight.getDescription());
    }
}
