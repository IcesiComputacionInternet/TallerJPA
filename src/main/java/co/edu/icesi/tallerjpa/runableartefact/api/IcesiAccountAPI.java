package co.edu.icesi.tallerjpa.runableartefact.api;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
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

        @PatchMapping("/activate")
        String activateAccount(@RequestBody String accountNumber);

        @PatchMapping("/deactivate")
        String deactivateAccount(@RequestBody String accountNumber);

        @PatchMapping("/withdrawal")
        String withdrawal(@RequestBody Map<String, String> withdrawalInformation);

        @PatchMapping("/deposit")
        String deposit(@RequestBody Map<String, String> depositInformation);

        @PatchMapping("/transfer")
        TransactionInformationResponseDTO transfer(@RequestBody Map<String,String> transferInformation);
}
