package co.edu.icesi.tallerjpa.runableartefact.unit.service.matcher;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    IcesiAccount icesiAccountLeft;

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null &&
                icesiAccountRight.getAccountNumber() != null &&
                Objects.equals(icesiAccountRight.getBalance(), icesiAccountLeft.getBalance()) &&
                Objects.equals(icesiAccountRight.getType(), icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.getUser().getUserId(), icesiAccountLeft.getUser().getUserId());
    }
}
