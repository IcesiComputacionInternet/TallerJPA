package icesi.university.accountSystem.strategy.interfaces;

import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.model.IcesiAccount;

public interface TypeAccountStrategy {

    TypeAccount getType();
    void withdraw(Long amount, IcesiAccount account);
    void transfer(Long amount, IcesiAccount accountOrigin, IcesiAccount accountDestination, boolean isReceiverAccountValid);
    boolean isReceiverAccountValid();
    void deposit(Long amount, IcesiAccount account);

}
