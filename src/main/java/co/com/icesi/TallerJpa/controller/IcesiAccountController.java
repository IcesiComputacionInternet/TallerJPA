package co.com.icesi.TallerJpa.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiAccountApi;
import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.dto.TransactionDTO;
import co.com.icesi.TallerJpa.security.IcesiSecurityContext;
import co.com.icesi.TallerJpa.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements IcesiAccountApi {

    private IcesiAccountService icesiAccountService;

    @Override
    public IcesiAccountResponseDTO addIcesiAccount(IcesiAccountRequestDTO icesiAccountRequestDTO) {
        return icesiAccountService.saveAccount(icesiAccountRequestDTO);
    }

    @Override
    public List<IcesiAccountResponseDTO> getAllAccounts() {
        return icesiAccountService.getAllAccounts();
    }

    @Override
    public IcesiAccountResponseDTO getIcesiAccount(String accountNumber) {
        return icesiAccountService.getAccountByAccountNumber(accountNumber);
    }

    @Override
    public List<IcesiAccountResponseDTO> getAllAccountsByUserId(String userId) {
        return icesiAccountService.getAllAccountsByUserId(UUID.fromString(userId));
    }

    @Override
    public List<IcesiAccountResponseDTO> getAllAccountsByEmail(@Email String email) {
        return icesiAccountService.getAllAccountsByEmail(email);
    }

    @Override
    public TransactionDTO enableAccount(String accountNumber) {
        UUID userId = UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        return icesiAccountService.enableAccount(accountNumber, userId);
    }

    @Override
    public TransactionDTO disableAccount(String accountNumber) {
        UUID userId = UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        return icesiAccountService.disableAccount(accountNumber, userId);
    }

    @Override
    public TransactionDTO withdraw(TransactionDTO transactionDTO) {
        UUID userId = UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        return icesiAccountService.withdrawal(transactionDTO, userId);
    }

    @Override
    public TransactionDTO deposit(TransactionDTO transactionDTO) {
        UUID userId = UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        return icesiAccountService.deposit(transactionDTO, userId);
    }

    @Override
    public TransactionDTO transfer(TransactionDTO transactionDTO) {
        UUID userId = UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        return icesiAccountService.transfer(transactionDTO, userId);
    }
}
