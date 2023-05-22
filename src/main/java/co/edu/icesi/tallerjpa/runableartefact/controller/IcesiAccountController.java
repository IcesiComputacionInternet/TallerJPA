package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.api.IcesiAccountAPI;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountUpdateDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.TransactionInformationResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements IcesiAccountAPI {

    private final IcesiAccountService icesiAccountService;

    public String createAccount(@RequestBody IcesiAccountDTO icesiAccountDTO) {
        return icesiAccountService.saveNewAccount(icesiAccountDTO);
    }
    @Override
    public IcesiAccountDTO updateAccount(IcesiAccountUpdateDTO icesiAccountUpdateDTO) {
        return icesiAccountService.updateAccount(icesiAccountUpdateDTO);
    }
    public String activateAccount(@RequestBody TransactionInformationDTO transactionInformationDTO) {
        return icesiAccountService.activateAccount(transactionInformationDTO);
    }

    public String deactivateAccount(@RequestBody TransactionInformationDTO transactionInformationDTO) {
        return icesiAccountService.deactivateAccount(transactionInformationDTO);
    }

    @Override
    public TransactionInformationResponseDTO withdrawal(TransactionInformationDTO withdrawalInformation) {
        return icesiAccountService.withdrawal(withdrawalInformation);
    }


    public String deposit(@RequestBody TransactionInformationDTO depositInformation) {
        return icesiAccountService.deposit(depositInformation.getAccountNumberOrigin(), depositInformation.getAmount());
    }

    //TODO: Cambiar el tipo de dato que recibe por un DTO
    public TransactionInformationResponseDTO transfer(@RequestBody TransactionInformationDTO transferInformation) {
        return icesiAccountService.transfer(transferInformation);
    }

}
