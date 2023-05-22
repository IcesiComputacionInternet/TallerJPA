package co.edu.icesi.tallerJPA.unit.matcher;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;



import java.util.Objects;


@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser leftIcesiUser;
    @Override

    public boolean matches(IcesiUser rightIcesiUser) {
        return rightIcesiUser.getUserId() != null &&

                Objects.equals(rightIcesiUser.getFirstName(), leftIcesiUser.getFirstName()) &&
                Objects.equals(rightIcesiUser.getLastName(), leftIcesiUser.getLastName()) &&
                Objects.equals(rightIcesiUser.getPassword(), leftIcesiUser.getPassword()) &&
                Objects.equals(rightIcesiUser.getEmail(), leftIcesiUser.getEmail()) &&
                Objects.equals(rightIcesiUser.getPhoneNumber(), leftIcesiUser.getPhoneNumber());
    }
}
