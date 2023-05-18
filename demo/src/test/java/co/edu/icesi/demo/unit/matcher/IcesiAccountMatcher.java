package co.edu.icesi.demo.unit.matcher;

import co.edu.icesi.demo.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    public IcesiAccountMatcher(IcesiAccount icesiAccountLeft){
        this.icesiAccountLeft = icesiAccountLeft;


    }

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId()!=null &&
                Objects.equals(icesiAccountRight.getAccountNumber(),icesiAccountLeft.getAccountNumber()) &&
                Objects.equals(icesiAccountRight.getBalance(), icesiAccountLeft.getBalance()) &&
                Objects.equals(icesiAccountRight.isActive(), icesiAccountLeft.isActive());

    }

}
