package co.edu.icesi.tallerjpa.init_test.matchers;


import co.edu.icesi.tallerjpa.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class AccountMatcher implements ArgumentMatcher<IcesiAccount> {

    private IcesiAccount icesiAccountLeft;

    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId() != null && icesiAccountRight.getAccountNumber() != null &&
                icesiAccountRight.getBalance() == icesiAccountLeft.getBalance() &&
                icesiAccountRight.getType().equals(icesiAccountLeft.getType()) &&
                icesiAccountRight.getUser().getUserId().equals(icesiAccountLeft.getUser().getUserId());
    }
}
