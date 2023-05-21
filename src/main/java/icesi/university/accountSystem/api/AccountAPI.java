package icesi.university.accountSystem.api;

import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.ResponseAccountDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.dto.TransactionResultDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";


    ResponseAccountDTO add(@Valid @RequestBody RequestAccountDTO account);


    TransactionResultDTO withdraw(@Valid @RequestBody TransactionOperationDTO transactionDTO);


    TransactionResultDTO deposit(@Valid @RequestBody TransactionOperationDTO transactionDTO);


    TransactionResultDTO transfer(@Valid @RequestBody TransactionOperationDTO transactionDTO);


    String activateAccount(@PathVariable String accountNumber);

    
    String deactivateAccount(@PathVariable String accountNumber);

}
