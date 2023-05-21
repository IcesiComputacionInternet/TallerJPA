package com.edu.icesi.TallerJPA.unit.matcher;

import com.edu.icesi.TallerJPA.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    public IcesiUserMatcher(IcesiUser icesiUserLeft){
        this.icesiUserLeft = icesiUserLeft;
    }

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null &&
                Objects.equals(icesiUserLeft.getFirstName(), icesiUserRight.getFirstName()) &&
                Objects.equals(icesiUserLeft.getLastName(), icesiUserRight.getLastName()) &&
                Objects.equals(icesiUserLeft.getEmail(), icesiUserRight.getEmail()) &&
                Objects.equals(icesiUserLeft.getPhoneNumber(), icesiUserRight.getPhoneNumber()) &&
                Objects.equals(icesiUserLeft.getPassword(), icesiUserRight.getPassword()) &&
                Objects.equals(icesiUserLeft.getIcesiRole(), icesiUserRight.getIcesiRole());
    }
}
