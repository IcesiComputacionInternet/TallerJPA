package com.edu.icesi.TallerJPA.unit.matcher;

import com.edu.icesi.TallerJPA.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    public IcesiAccountMatcher(IcesiAccount icesiAccountLeft){
        this.icesiAccountLeft = icesiAccountLeft;
    }

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null &&
                Objects.equals(icesiAccountLeft.getBalance(), icesiAccountRight.getBalance()) &&
                Objects.equals(icesiAccountLeft.getType(), icesiAccountRight.getType()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser(), icesiAccountRight.getIcesiUser());
    }
}
