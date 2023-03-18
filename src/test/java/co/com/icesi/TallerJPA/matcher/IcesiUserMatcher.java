package co.com.icesi.TallerJPA.matcher;

import co.com.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {
    private IcesiUser icesiUserLeft;
    @Override
    public boolean matches(IcesiUser icesiUserRigth) {
        return icesiUserRigth.getFirstName().equals(icesiUserLeft.getFirstName()) &&
                icesiUserRigth.getLastName().equals(icesiUserLeft.getLastName()) &&
                icesiUserRigth.getPhoneNumber().equals(icesiUserLeft.getPhoneNumber()) &&
                icesiUserRigth.getPassword().equals(icesiUserLeft.getPassword()) &&
                icesiUserRigth.getEmail().equals(icesiUserLeft.getEmail());
    }
}
