package com.example.jpa.matcher;

import com.example.jpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private final IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserLeft.getFirstName().equals(icesiUserRight.getFirstName()) &&
                icesiUserLeft.getLastName().equals(icesiUserRight.getLastName()) &&
                icesiUserLeft.getEmail().equals(icesiUserRight.getEmail()) &&
                icesiUserLeft.getPhoneNumber().equals(icesiUserRight.getPhoneNumber()) &&
                icesiUserLeft.getPassword().equals(icesiUserRight.getPassword()) &&
                icesiUserLeft.getRole().equals(icesiUserRight.getRole());
    }
}
