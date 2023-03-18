package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private final IcesiAccount icesiAccountLeft;


    public IcesiAccountMatcher(IcesiAccount acc) {
        this.icesiAccountLeft = acc;
    }

    @Override
    public boolean matches(IcesiAccount icesiAccountRight){
        return icesiAccountRight.getAccountId() != null &&
                icesiAccountRight.getAccountNumber() != null &&
                Objects.equals(icesiAccountRight.getBalance(), icesiAccountLeft.getBalance()) &&
                Objects.equals(icesiAccountRight.getType(), icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.isActive(), icesiAccountLeft.isActive());
    }
}
