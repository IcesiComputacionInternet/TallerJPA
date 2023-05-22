package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private final IcesiAccount icesiAccountLeft;

    @Override
    public boolean matches(IcesiAccount icesiAccountRight){
        return icesiAccountRight.getAccountId() != null && icesiAccountRight.getAccountNumber() != null &&
                icesiAccountRight.getBalance().equals(icesiAccountLeft.getBalance()) &&
                icesiAccountRight.getType().equals(icesiAccountLeft.getType()) &&
                icesiAccountRight.isActive() == icesiAccountLeft.isActive() &&
                icesiAccountRight.getUser().getUserId().equals(icesiAccountLeft.getUser().getUserId());
    }
}
