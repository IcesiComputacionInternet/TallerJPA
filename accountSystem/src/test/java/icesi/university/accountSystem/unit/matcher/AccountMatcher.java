package icesi.university.accountSystem.unit.matcher;

import icesi.university.accountSystem.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount accountLeft;

    @Override
    public boolean matches(IcesiAccount accountRight) {
        return accountRight.getAccountId() != null && accountRight.getAccountNumber() != null &&
                accountRight.getBalance() == accountLeft.getBalance() &&
                accountRight.getType().equals(accountLeft.getType()) &&
                accountRight.isActive() == accountLeft.isActive() &&
                accountRight.getUser().getUserId().equals(accountLeft.getUser().getUserId());
    }
}
