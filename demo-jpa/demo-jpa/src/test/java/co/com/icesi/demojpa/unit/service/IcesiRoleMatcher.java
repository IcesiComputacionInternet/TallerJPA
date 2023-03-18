package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private final IcesiRole icesiRoleleft;

    public IcesiRoleMatcher(IcesiRole role){
        this.icesiRoleleft = role;
    }

    @Override
    public boolean matches(IcesiRole icesiRoleRight){
        return icesiRoleRight.getRoleId() != null &&
                Objects.equals(icesiRoleRight.getName(), icesiRoleleft.getName()) &&
                Objects.equals(icesiRoleRight.getDescription(), icesiRoleleft.getDescription());
    }
}
