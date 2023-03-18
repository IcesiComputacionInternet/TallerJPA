package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.model.IcesiAccount;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class IcesiAccountMatcher implements ArgumentMatcher<IcesiAccount> {

   private IcesiAccount icesiAccountLeft;

   public IcesiAccountMatcher(IcesiAccount icesiAccountLeft){
       this.icesiAccountLeft=icesiAccountLeft;
   }
    @Override
    public boolean matches(IcesiAccount icesiAccountRight) {
        return icesiAccountRight.getAccountId()!=null &&
                icesiAccountRight.getAccountNumber()!=null &&
                Objects.equals(icesiAccountRight.getType(),icesiAccountLeft.getType()) &&
                Objects.equals(icesiAccountRight.isActive(), icesiAccountLeft.isActive()) &&
                Objects.equals(icesiAccountRight.getBalance(),icesiAccountLeft.getBalance());
    }
}
