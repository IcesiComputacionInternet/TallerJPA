package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {
    IcesiRole icesiRoleRight;
    @Override
    public boolean matches(IcesiRole icesiRoleLeft) {
        return icesiRoleLeft.getRoleId() != null &&
                Objects.equals(icesiRoleLeft.getIcesiUsers(), icesiRoleRight.getIcesiUsers()) &&
                Objects.equals(icesiRoleLeft.getDescription(), icesiRoleRight.getDescription()) &&
                Objects.equals(icesiRoleLeft.getName(), icesiRoleRight.getName());
    }
}
