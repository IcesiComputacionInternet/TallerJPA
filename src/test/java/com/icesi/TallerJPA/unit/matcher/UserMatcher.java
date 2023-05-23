package com.icesi.TallerJPA.unit.matcher;

import com.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;
    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() == null && icesiUserRight.getIcesiRole() == null &&
                icesiUserRight.getFirstName().equals(icesiUserLeft.getFirstName()) &&
                icesiUserRight.getLastName().equals(icesiUserLeft.getLastName()) &&
                icesiUserRight.getEmail().equals(icesiUserLeft.getEmail()) &&
                icesiUserRight.getPassword().equals(icesiUserLeft.getPassword()) &&
                icesiUserRight.getPhoneNumber().equals(icesiUserLeft.getPhoneNumber());
    }


    }
    /*
        private IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null && icesiUserRight.getIcesiRole() != null &&
                icesiUserRight.getFirstName().equals(icesiUserLeft.getFirstName()) &&
                icesiUserRight.getLastName().equals(icesiUserLeft.getLastName()) &&
                icesiUserRight.getEmail().equals(icesiUserLeft.getEmail()) &&
                icesiUserRight.getPassword().equals(icesiUserLeft.getPassword()) &&
                icesiUserRight.getPhoneNumber().equals(icesiUserLeft.getPhoneNumber());
    }
     */
