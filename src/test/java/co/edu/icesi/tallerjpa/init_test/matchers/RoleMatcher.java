package co.edu.icesi.tallerjpa.init_test.matchers;


import co.edu.icesi.tallerjpa.model.IcesiRole;
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
