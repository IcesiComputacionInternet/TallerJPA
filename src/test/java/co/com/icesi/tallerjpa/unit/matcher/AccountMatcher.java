package co.com.icesi.tallerjpa.unit.matcher;

import co.com.icesi.tallerjpa.model.Account;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<Account> {

        private Account accountLeft;

        @Override
        public boolean matches(Account accountRight) {
            return accountRight.getAccountId() != null &&
                    accountRight.getBalance().equals(accountLeft.getBalance()) &&
                    accountRight.getType().equals(accountLeft.getType()) &&
                    accountRight.isActive() == accountLeft.isActive();
        }
}
