package co.edu.icesi.tallerJPA.unit.matcher;

import co.edu.icesi.tallerJPA.model.Account;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<Account> {

    private Account leftAccount;

    @Override
    public boolean matches(Account rightAccount) {
        return rightAccount.getId() != null &&
                rightAccount.getBalance() == (leftAccount.getBalance()) &&
                rightAccount.getType().equals(leftAccount.getType()) &&
                rightAccount.isActive() == leftAccount.isActive();
    }
}
