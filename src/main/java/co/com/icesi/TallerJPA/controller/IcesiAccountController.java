package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiAccountAPI;
import co.com.icesi.TallerJPA.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import co.com.icesi.TallerJPA.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiAccountAPI.BASE_ACCOUNT_URL;

@RestController
@RequestMapping(BASE_ACCOUNT_URL)
@AllArgsConstructor
public class IcesiAccountController implements IcesiAccountAPI {
    private IcesiAccountService accountService;

    @Override
    public List<IcesiAccountCreateResponseDTO> getAllAccounts() {
        return null;
    }

    @Override
    public IcesiAccountCreateDTO getAccountByNumber(String accountNumber) {
        return null;
    }

    @Override
    public IcesiAccountCreateResponseDTO addAccount(IcesiAccountCreateDTO accountDto) {
        return accountService.save(accountDto);
    }

    @Override
    public IcesiTransactionDTO depositOnlyMoney(IcesiTransactionDTO transactionDTODeposit) {
        return accountService.depositOnly(transactionDTODeposit);
    }

    @Override
    public IcesiTransactionDTO withdrawMoney(IcesiTransactionDTO transactionDTOWithdraw) {
        return accountService.withdrawal(transactionDTOWithdraw);
    }

    @Override
    public IcesiTransactionDTO transferMoney(IcesiTransactionDTO transactionDTOMoney) {
        return accountService.transferMoney(transactionDTOMoney);
    }

    @Override
    public String enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public String disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }
}
