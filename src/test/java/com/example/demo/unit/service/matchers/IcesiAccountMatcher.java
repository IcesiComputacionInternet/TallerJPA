package com.example.demo.unit.service.matchers;

import java.util.Objects;

import org.mockito.ArgumentMatcher;

import com.example.demo.model.IcesiAccount;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {
    
    private IcesiAccount icesiAccount2;

    @Override   
    public boolean matches(IcesiAccount icesiAccount1) {
        return icesiAccount1.getAccountId() != null &&
            icesiAccount1.getAccountNumber() != null &&
            Objects.equals(icesiAccount1.getBalance(), icesiAccount2.getBalance()) &&
            Objects.equals(icesiAccount1.getType(), icesiAccount2.getType()) &&
            Objects.equals(icesiAccount1.isActive(), icesiAccount2.isActive()) &&
            Objects.equals(icesiAccount1.getIcesiUser().getFirstName(), icesiAccount2.getIcesiUser().getFirstName()) &&
            Objects.equals(icesiAccount1.getIcesiUser().getLastName(), icesiAccount2.getIcesiUser().getLastName()) &&
            Objects.equals(icesiAccount1.getIcesiUser().getEmail(), icesiAccount2.getIcesiUser().getEmail());
    }
}
