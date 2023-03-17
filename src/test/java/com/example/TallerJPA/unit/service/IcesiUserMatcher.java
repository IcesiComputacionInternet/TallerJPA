package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null &&
                icesiUserRight.getFirstName().equals(icesiUserLeft.getFirstName()) &&
                icesiUserRight.getLastName().equals(icesiUserLeft.getLastName()) &&
                icesiUserRight.getPassword().equals(icesiUserLeft.getPassword()) &&
                icesiUserRight.getEmail().equals(icesiUserLeft.getEmail()) &&
                icesiUserRight.getPhoneNumber().equals(icesiUserLeft.getPhoneNumber()) &&
                icesiUserRight.getRole().equals(icesiUserLeft.getRole());
    }
}
