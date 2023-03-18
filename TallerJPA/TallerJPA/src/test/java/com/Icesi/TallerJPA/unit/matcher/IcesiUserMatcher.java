package com.Icesi.TallerJPA.unit.matcher;

import com.Icesi.TallerJPA.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserOne;

    public IcesiUserMatcher(IcesiUser icesiUserOne){
        this.icesiUserOne = icesiUserOne;
    }

    @Override
    public boolean matches(IcesiUser icesiUser) {
        return icesiUser.getUserId() != null &&
                Objects.equals(icesiUserOne.getFirstName(), icesiUser.getFirstName()) &&
                Objects.equals(icesiUserOne.getLastName(), icesiUser.getLastName()) &&
                Objects.equals(icesiUserOne.getPhoneNumber(), icesiUser.getPhoneNumber()) &&
                Objects.equals(icesiUserOne.getPassword(), icesiUser.getPassword()) &&
                Objects.equals(icesiUserOne.getIcesiRole(), icesiUser.getIcesiRole());
    }
}
