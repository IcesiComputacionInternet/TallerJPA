package co.com.icesi.TallerJPA.unit.matcher;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;
    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getBalance().equals(icesiAccountLeft.getBalance()) &&
               icesiAccountRight.getType().equals(icesiAccountLeft.getType()) &&
                (Objects.equals(icesiAccountRight.isActive(),icesiAccountLeft.isActive()))  &&
                icesiAccountRight.getUser().getUserID().equals(icesiAccountLeft.getUser().getUserID());

    }
}
