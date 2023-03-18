package com.example.jpa.matcher;

import com.example.jpa.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class RoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    @Override
    public boolean matches(IcesiRole icesiRoleRight) {
        System.out.println("RoleMatcher: " + icesiRoleLeft.getName() + " " + icesiRoleRight.getName());
        System.out.println("RoleMatcher: " + icesiRoleLeft.getDescription() + " " + icesiRoleRight.getDescription());
        return Objects.equals(icesiRoleRight.getName(), icesiRoleLeft.getName()) &&
                Objects.equals(icesiRoleRight.getDescription(), icesiRoleLeft.getDescription());
    }
}

