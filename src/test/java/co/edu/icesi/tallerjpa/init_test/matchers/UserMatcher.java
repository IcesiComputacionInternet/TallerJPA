package co.edu.icesi.tallerjpa.init_test.matchers;

import co.edu.icesi.tallerjpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;
    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() == null && icesiUserRight.getIcesirole() == null &&
                icesiUserRight.getFirstName().equals(icesiUserLeft.getFirstName()) &&
                icesiUserRight.getLastName().equals(icesiUserLeft.getLastName()) &&
                icesiUserRight.getEmail().equals(icesiUserLeft.getEmail()) &&
                icesiUserRight.getPassword().equals(icesiUserLeft.getPassword()) &&
                icesiUserRight.getPhoneNumber().equals(icesiUserLeft.getPhoneNumber());
    }


    }

