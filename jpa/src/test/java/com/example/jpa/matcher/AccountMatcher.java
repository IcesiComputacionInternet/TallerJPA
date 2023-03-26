package com.example.jpa.matcher;

import com.example.jpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private final IcesiAccount accountLeft;

    @Override
    public boolean matches(IcesiAccount accountRight) {
        return accountRight.getBalance().equals(accountLeft.getBalance()) &&
                accountRight.getType().equals(accountLeft.getType()) &&
                (Objects.equals(accountRight.isActive(),accountLeft.isActive()))  &&
                accountRight.getUser().getUserId().equals(accountLeft.getUser().getUserId());
    }
}

