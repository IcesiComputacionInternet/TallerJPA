package co.edu.icesi.tallerJPA.unit.matcher;

import co.edu.icesi.tallerJPA.model.Role;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class RoleMatcher implements ArgumentMatcher<Role> {

    private Role leftRole;

    @Override
    public boolean matches(Role rightRole) {
        return rightRole.getRoleId() != null &&
                rightRole.getName().equals(leftRole.getName()) &&
                rightRole.getDescription().equals(leftRole.getDescription());
    }
}
