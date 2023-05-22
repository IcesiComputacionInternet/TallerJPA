package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private final IcesiUser userLeft;

    @Override
    public boolean matches(IcesiUser userRight) {
        return userRight.getUserId() != null &&
                Objects.equals(userRight.getFirstName(), userLeft.getFirstName()) &&
                Objects.equals(userRight.getLastName(), userLeft.getLastName()) &&
                Objects.equals(userRight.getPassword(), userLeft.getPassword()) &&
                Objects.equals(userRight.getEmail(), userLeft.getEmail());
    }
}
