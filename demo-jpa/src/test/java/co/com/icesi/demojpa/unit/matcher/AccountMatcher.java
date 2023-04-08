package co.com.icesi.demojpa.unit.matcher;

import co.com.icesi.demojpa.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount accountLeft;

    public AccountMatcher(IcesiAccount accountLeft){
        this.accountLeft = accountLeft;
    }
    @Override
    public boolean matches(IcesiAccount accountRight) {
        return accountRight.getAccountId()!=null &&
                accountRight.getAccountNumber()!=null &&
                Objects.equals(accountRight.getType(), accountLeft.getType()) &&
                Objects.equals(accountRight.isActive(), accountLeft.isActive()) &&
                Objects.equals(accountRight.getBalance(), accountLeft.getBalance());
    }
}
