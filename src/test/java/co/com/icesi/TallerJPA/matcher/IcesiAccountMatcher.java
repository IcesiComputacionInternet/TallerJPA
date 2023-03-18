package co.com.icesi.TallerJPA.matcher;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;
    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getBalance().equals(icesiAccountLeft.getBalance()) &&
               icesiAccountRight.getType().equals(icesiAccountLeft.getType()) &&
                (icesiAccountLeft.isActive() && icesiAccountRight.isActive())  &&
                icesiAccountRight.getUser().getUserID().equals(icesiAccountLeft.getUser().getUserID());

    }
}
