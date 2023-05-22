package co.com.icesi.demojpa.unit.service;


import co.com.icesi.demojpa.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private final IcesiUser icesiUserLeft;

    public IcesiUserMatcher(IcesiUser icesiUser1) {
        icesiUserLeft=icesiUser1;
    }

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null &&
                Objects.equals(icesiUserRight.getFirstName(), icesiUserLeft.getFirstName()) &&
                Objects.equals(icesiUserRight.getLastName(), icesiUserLeft.getLastName()) &&
                Objects.equals(icesiUserRight.getPassword(), icesiUserLeft.getPassword()) &&
                Objects.equals(icesiUserRight.getEmail(), icesiUserLeft.getEmail());
    }


}
