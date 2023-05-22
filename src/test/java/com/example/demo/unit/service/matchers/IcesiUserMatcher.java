package com.example.demo.unit.service.matchers;

import java.util.Objects;

import org.mockito.ArgumentMatcher;

import com.example.demo.model.IcesiUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {
    
    private IcesiUser icesiUser2;

    @Override
    public boolean matches(IcesiUser icesiUser1) {
        return icesiUser1.getUserId() != null &&
            Objects.equals(icesiUser1.getFirstName(), icesiUser2.getFirstName()) &&
            Objects.equals(icesiUser1.getLastName(), icesiUser2.getLastName()) &&
            Objects.equals(icesiUser1.getEmail(), icesiUser2.getEmail()) &&
            Objects.equals(icesiUser1.getPhoneNumber(), icesiUser2.getPhoneNumber()) &&
            Objects.equals(icesiUser1.getPassword(), icesiUser2.getPassword()) &&
            Objects.equals(icesiUser1.getIcesiRole().getName(), icesiUser2.getIcesiRole().getName());
    }
}
