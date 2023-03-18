package icesi.university.accountSystem.unit.matcher;

import icesi.university.accountSystem.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser UserLeft;

    @Override
    public boolean matches(IcesiUser UserRight) {
        return UserRight.getUserId() != null && UserRight.getRole() != null &&
                Objects.equals(UserRight.getFirstName(), UserLeft.getFirstName()) &&
                Objects.equals(UserRight.getLastName(), UserLeft.getLastName()) &&
                Objects.equals(UserRight.getPassword(), UserLeft.getPassword()) &&
                Objects.equals(UserRight.getEmail(), UserLeft.getEmail()) &&
                Objects.equals(UserRight.getPhoneNumber(), UserLeft.getPhoneNumber());
    }
}
