package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {
    IcesiAccount icesiAccountRight;

    @Override
    public boolean matches(IcesiAccount icesiAccountLeft) {
        return icesiAccountLeft.getAccountId() != null &&
                icesiAccountLeft.getAccountNumber() != null &&
                Objects.equals(icesiAccountLeft.getBalance(), icesiAccountRight.getBalance()) &&
                Objects.equals(icesiAccountLeft.getType(), icesiAccountRight.getType()) &&
                Objects.equals(icesiAccountLeft.isActive(), icesiAccountRight.isActive()) &&
                Objects.equals(icesiAccountLeft.getBalance(), icesiAccountRight.getBalance()) &&
                Objects.equals(icesiAccountLeft.getBalance(), icesiAccountRight.getBalance()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getFirstName(), icesiAccountRight.getIcesiUser().getFirstName()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getLastName(), icesiAccountRight.getIcesiUser().getLastName()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getEmail(), icesiAccountRight.getIcesiUser().getEmail()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getPhoneNumber(), icesiAccountRight.getIcesiUser().getPhoneNumber()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getPassword(), icesiAccountRight.getIcesiUser().getPassword()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getIcesiRole().getName(), icesiAccountRight.getIcesiUser().getIcesiRole().getName()) &&
                Objects.equals(icesiAccountLeft.getIcesiUser().getIcesiRole().getDescription(), icesiAccountRight.getIcesiUser().getIcesiRole().getDescription());
    }
}
