package co.edu.icesi.tallerjpa.runableartefact.unit.service;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.mockito.ArgumentMatcher;

public class IcesiUserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser icesiUserRight) {
        return icesiUserRight.getUserId() != null;
        //add all class attributs
    }
}
