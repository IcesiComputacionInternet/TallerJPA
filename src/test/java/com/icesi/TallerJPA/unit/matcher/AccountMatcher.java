package com.icesi.TallerJPA.unit.matcher;

import com.icesi.TallerJPA.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null && icesiAccountRight.getAccountNumber() != null &&
                icesiAccountRight.getBalance() == icesiAccountLeft.getBalance() &&
                icesiAccountRight.getType().equals(icesiAccountLeft.getType()) &&
                icesiAccountRight.getActive().equals(icesiAccountLeft.getActive()) &&
                icesiAccountRight.getIcesiUser().getUserId().equals(icesiAccountLeft.getIcesiUser().getUserId());
    }
}
