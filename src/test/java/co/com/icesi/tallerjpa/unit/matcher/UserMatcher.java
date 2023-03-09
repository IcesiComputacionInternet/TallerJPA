package co.com.icesi.tallerjpa.unit.matcher;

import co.com.icesi.tallerjpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null &&
                Objects.equals(icesiUserRight.getFirstName(), icesiUserLeft.getFirstName()) &&
                Objects.equals(icesiUserRight.getLastName(), icesiUserLeft.getLastName()) &&
                Objects.equals(icesiUserRight.getPassword(), icesiUserLeft.getPassword()) &&
                Objects.equals(icesiUserRight.getEmail(), icesiUserLeft.getEmail()) &&
                Objects.equals(icesiUserRight.getPhoneNumber(), icesiUserLeft.getPhoneNumber());
    }
}
