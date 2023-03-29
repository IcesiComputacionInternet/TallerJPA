package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.api.IcesiAccountAPI;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
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

    public String activateAccount(@RequestBody String accountNumber) {
        return icesiAccountService.activateAccount(accountNumber);
    }

    public String deactivateAccount(@RequestBody String accountNumber) {
        return icesiAccountService.deactivateAccount(accountNumber);
    }

    public String withdrawal(@RequestBody Map<String, String> withdrawalInformation) {
        return icesiAccountService.withdrawal(withdrawalInformation.get("accountNumber")
                , Long.valueOf(withdrawalInformation.get("amount")));
    }

    public String deposit(@RequestBody Map<String, String> depositInformation) {
        return icesiAccountService.deposit(depositInformation.get("accountNumber"), Long.valueOf(depositInformation.get("amount")));
    }

    //TODO: Cambiar el tipo de dato que recibe por un DTO
    public String transfer(@RequestBody Map<String,String> transferInformation) {
        return icesiAccountService.transfer(transferInformation.get("accountNumberOrigin")
                , transferInformation.get("accountNumberDestination")
                , Long.valueOf(transferInformation.get("amount")));
    }

}
