package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private final IcesiUser userLeft;

    public IcesiUserMatcher(IcesiUser iu){
        userLeft = iu;
    }

    @Override
    public boolean matches(IcesiUser userRight) {
        return userRight.getUserId() != null &&
                Objects.equals(userRight.getFirstName(), userLeft.getFirstName()) &&
                Objects.equals(userRight.getLastName(), userLeft.getLastName()) &&
                Objects.equals(userRight.getPassword(), userLeft.getPassword()) &&
                Objects.equals(userRight.getEmail(), userLeft.getEmail());
    }
}
