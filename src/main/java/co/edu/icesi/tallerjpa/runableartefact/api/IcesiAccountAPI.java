package co.edu.icesi.tallerjpa.runableartefact.api;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountUpdateDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.TransactionInformationResponseDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(IcesiAccountAPI.BASE_URL)
public interface IcesiAccountAPI {

        String BASE_URL = "api/icesi-accounts";

        @PostMapping("/create")
        String createAccount(IcesiAccountDTO icesiAccountDTO);

        @PostMapping("/update")
        IcesiAccountDTO updateAccount(IcesiAccountUpdateDTO icesiAccountUpdateDTO);

        @PatchMapping("/activate")
        String activateAccount(@RequestBody TransactionInformationDTO transactionInformationDTO);

        @PatchMapping("/deactivate")
        String deactivateAccount(@RequestBody TransactionInformationDTO transactionInformationDTO);

        @PatchMapping("/withdrawal")
        TransactionInformationResponseDTO withdrawal(@RequestBody TransactionInformationDTO withdrawalInformation);

        @PatchMapping("/deposit")
        String deposit(@RequestBody TransactionInformationDTO depositInformation);

        @PatchMapping("/transfer")
        TransactionInformationResponseDTO transfer(@RequestBody TransactionInformationDTO transferInformation);
}
