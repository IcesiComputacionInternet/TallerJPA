package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class UserMatcher implements ArgumentMatcher<IcesiUser> {


    private IcesiUser userLeft;

    public UserMatcher(IcesiUser userLeft){
        this.userLeft =userLeft;
    }

    @Override
    public boolean matches(IcesiUser userRight) {
        return userRight.getUserId() != null && userRight.getRole() != null &&
                Objects.equals(userRight.getFirstName(), userLeft.getFirstName()) &&
                Objects.equals(userRight.getLastName(), userLeft.getLastName()) &&
                Objects.equals(userRight.getPassword(), userLeft.getPassword()) &&
                Objects.equals(userRight.getEmail(), userLeft.getEmail()) &&
                Objects.equals(userRight.getPhoneNumber(), userLeft.getPhoneNumber());
    }
}
