package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    public IcesiAccountMatcher(IcesiAccount icesiAccountLeft) {
        this.icesiAccountLeft = icesiAccountLeft;
    }

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null &&
                icesiAccountRight.getAccountNumber()!=null &&
                Objects.equals(icesiAccountRight.getUser().getEmail(), icesiAccountLeft.getUser().getEmail()) &&
                Objects.equals(icesiAccountRight.getBalance(), icesiAccountLeft.getBalance()) &&
                Objects.equals(icesiAccountRight.getType(), icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.isActive(), icesiAccountLeft.isActive());
    }
}
