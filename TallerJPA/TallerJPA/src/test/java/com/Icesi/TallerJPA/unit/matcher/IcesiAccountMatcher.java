package com.Icesi.TallerJPA.unit.matcher;

import com.Icesi.TallerJPA.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccount;

    public IcesiAccountMatcher(IcesiAccount icesiAccount){
        this.icesiAccount = icesiAccount;
    }

    @Override
    public boolean matches(IcesiAccount icesiAccountOne) {
        return icesiAccountOne.getAccountId() != null &&
                Objects.equals(icesiAccount.getBalance(), icesiAccountOne.getBalance()) &&
                Objects.equals(icesiAccount.getType(), icesiAccountOne.getType()) &&
                Objects.equals(icesiAccount.getIcesiUser(), icesiAccountOne.getIcesiUser());
    }
}
