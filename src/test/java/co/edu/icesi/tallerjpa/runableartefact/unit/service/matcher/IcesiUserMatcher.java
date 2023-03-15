package co.edu.icesi.tallerjpa.runableartefact.unit.service.matcher;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser IcesiUserLeft;

    @Override
    public boolean matches(IcesiUser UserRight) {
        return UserRight.getUserId() != null && UserRight.getRole() != null &&
                Objects.equals(UserRight.getFirstName(), IcesiUserLeft.getFirstName()) &&
                Objects.equals(UserRight.getLastName(), IcesiUserLeft.getLastName()) &&
                Objects.equals(UserRight.getPassword(), IcesiUserLeft.getPassword()) &&
                Objects.equals(UserRight.getEmail(), IcesiUserLeft.getEmail()) &&
                Objects.equals(UserRight.getPhoneNumber(), IcesiUserLeft.getPhoneNumber());
    }
}
