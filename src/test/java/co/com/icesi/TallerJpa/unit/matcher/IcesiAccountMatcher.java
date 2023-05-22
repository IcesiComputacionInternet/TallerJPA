package co.com.icesi.TallerJpa.unit.matcher;

import co.com.icesi.TallerJpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;
@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {
    private IcesiAccount icesiAccountLeft;
    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return  Objects.equals(icesiAccountRight.getBalance(),icesiAccountLeft.getBalance()) &&
                Objects.equals(icesiAccountRight.getType(),icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.isActive(),icesiAccountLeft.isActive()) &&
                Objects.equals(icesiAccountRight.getIcesiUser().getEmail(),icesiAccountLeft.getIcesiUser().getEmail()) &&
                Objects.equals(icesiAccountRight.getIcesiUser().getPhoneNumber(),icesiAccountLeft.getIcesiUser().getPhoneNumber());
    }
}
