package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.model.IcesiUser;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    public IcesiUserMatcher(IcesiUser icesiUserLeft){
        this.icesiUserLeft = icesiUserLeft;
    }
    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId()!=null &&
                Objects.equals(icesiUserRight.getFirstname(), icesiUserLeft.getFirstname()) &&
                Objects.equals(icesiUserRight.getLastName(),icesiUserLeft.getLastName()) &&
                Objects.equals(icesiUserRight.getEmail(), icesiUserLeft.getEmail()) &&
                Objects.equals(icesiUserRight.getPhoneNumber(), icesiUserLeft.getPhoneNumber()) &&
                Objects.equals(icesiUserRight.getPassword(), icesiUserLeft.getPassword());
    }
}
