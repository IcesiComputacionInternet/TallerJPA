package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private final IcesiRole icesiRoleleft;

    @Override
    public boolean matches(IcesiRole icesiRoleRight){
        return icesiRoleRight.getRoleId() != null &&
                icesiRoleRight.getName().equals(icesiRoleleft.getName()) &&
                icesiRoleRight.getDescription().equals(icesiRoleleft.getDescription());
    }
}
