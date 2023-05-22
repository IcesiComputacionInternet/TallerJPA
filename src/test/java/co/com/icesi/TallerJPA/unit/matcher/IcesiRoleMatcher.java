package co.com.icesi.TallerJPA.unit.matcher;

import co.com.icesi.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole> {

    private IcesiRole icesiRoleLeft;

    public  IcesiRoleMatcher( IcesiRole icesiRoleLeft){
        this.icesiRoleLeft = icesiRoleLeft;
    }
    @Override
    public boolean matches(IcesiRole roleRigth) {
        return roleRigth.getName().equals(icesiRoleLeft.getName()) &&
                roleRigth.getDescription().equals(icesiRoleLeft.getDescription()) ;
    }
}
