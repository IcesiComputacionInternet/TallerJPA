package com.example.tallerjpa.unit.service.matcher;


import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;
    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null &&
                icesiAccountRight.getAccountNumber() != null &&
                icesiAccountRight.getBalance() == icesiAccountLeft.getBalance() &&
                Objects.equals(icesiAccountRight.getType(), icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.isActive(), icesiAccountLeft.isActive())&&
                Objects.equals(icesiAccountRight.getIcesiUser().getUserId(), icesiAccountLeft.getIcesiUser().getUserId());
    }

}
