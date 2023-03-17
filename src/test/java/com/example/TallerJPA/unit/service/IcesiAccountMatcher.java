package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;
@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountLeft.getAccountId() != null &&
                icesiAccountLeft.getAccountNumber() != null &&
                Objects.equals(icesiAccountLeft.getBalance(), icesiAccountRight.getBalance()) &&
                Objects.equals(icesiAccountLeft.getType(), icesiAccountRight.getType()) &&
                Objects.equals(icesiAccountLeft.getActive(), icesiAccountRight.getActive());
    }
}
