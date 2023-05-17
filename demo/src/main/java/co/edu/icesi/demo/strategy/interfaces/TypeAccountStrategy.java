package co.edu.icesi.demo.strategy.interfaces;

import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.model.IcesiAccount;

public interface TypeAccountStrategy {

    TypeAccount getType();
    void withdraw(Long amount, IcesiAccount account);
    void transfer(Long amount, IcesiAccount accountOrigin, IcesiAccount accountDestination, boolean isReceiverAccountValid);
    boolean isReceiverAccountValid();
    void deposit(Long amount, IcesiAccount account);

}
