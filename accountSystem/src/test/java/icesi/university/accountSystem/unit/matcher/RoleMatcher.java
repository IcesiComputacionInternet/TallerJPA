package icesi.university.accountSystem.unit.matcher;

import icesi.university.accountSystem.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;
@AllArgsConstructor
public class RoleMatcher implements ArgumentMatcher<IcesiRole> {
    private IcesiRole roleLeft;

    @Override
    public boolean matches(IcesiRole roleRight) {
        return roleRight.getRoleId() != null &&
                roleRight.getName().equals(roleLeft.getName()) &&
                roleRight.getDescription().equals(roleLeft.getDescription());
    }
}
