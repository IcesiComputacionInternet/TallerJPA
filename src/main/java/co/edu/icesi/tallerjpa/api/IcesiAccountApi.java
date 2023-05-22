package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.dto.IcesiAccountResponseDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IcesiAccountApi {
    String ACCOUNT_BASE_URL = "/account";

    @PostMapping
    IcesiAccountResponseDTO createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO);

    @PatchMapping("/activeAccount")
    String activeAccount(@PathVariable String accountNumber);

    @PatchMapping("/inactiveAccount")
    String inactiveAccount(@PathVariable String accountNumber);

    @PatchMapping("/withdrawal")
    String withdrawalAccount(@PathVariable String accountNumber, @RequestBody Long value);

    @PatchMapping("/deposit")
    String depositAccount(@PathVariable String accountNumber, @RequestBody Long value);

    @PatchMapping("/transfer")
    String transferAccount(@PathVariable String accountNumberOrigin, @PathVariable String accountNumberDestination, @RequestBody Long value);
}
