package co.com.icesi.tallerjpa.unit.matcher;

import co.com.icesi.tallerjpa.model.Role;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class RoleMatcher implements ArgumentMatcher<Role> {

    private Role roleLeft;

    @Override
    public boolean matches(Role roleRight) {
        return roleRight.getRoleId() != null &&
                roleRight.getName().equals(roleLeft.getName()) &&
                roleRight.getDescription().equals(roleLeft.getDescription());
    }
}
