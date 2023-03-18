package com.example.jpa.matcher;

import com.example.jpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private final IcesiAccount accountLeft;

    @Override
    public boolean matches(IcesiAccount accountRight) {
        return accountRight.getBalance() == accountLeft.getBalance() &&
                accountRight.getType().equals(accountLeft.getType()) &&
                (accountLeft.isActive() && accountRight.isActive())  &&
                accountRight.getUser().getUserId().equals(accountLeft.getUser().getUserId());
    }
}

