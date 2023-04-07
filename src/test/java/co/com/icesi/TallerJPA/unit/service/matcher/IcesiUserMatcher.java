package co.com.icesi.TallerJPA.unit.service.matcher;

import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {
    private IcesiUser icesiUserLeft;

    public IcesiUserMatcher(IcesiUser icesiUserLeft) {
        this.icesiUserLeft = icesiUserLeft;
    }

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId()!=null &&
                icesiUserRight.getRole()!=null &&
                Objects.equals(icesiUserRight.getFirstName(),icesiUserLeft.getFirstName()) &&
                Objects.equals(icesiUserRight.getLastname(),icesiUserLeft.getLastname()) &&
                Objects.equals(icesiUserRight.getPassword(),icesiUserLeft.getPassword()) &&
                Objects.equals(icesiUserRight.getEmail(),icesiUserLeft.getEmail()) &&
                Objects.equals(icesiUserRight.getPhoneNumber(),icesiUserLeft.getPhoneNumber());
    }
}
