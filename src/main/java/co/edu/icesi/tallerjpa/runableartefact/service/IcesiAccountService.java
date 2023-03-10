package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;

    private final IcesiAccountMapper icesiAccountMapper;

    public String saveNewAccount(IcesiAccountDTO icesiAccountDTO) {
        IcesiAccount icesiAccount = icesiAccountMapper.toIcesiAccount(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumberThatDontExist());
        return "Account saved";
    }

    private String generateAccountNumberThatDontExist() {
        boolean accountNumberExists = true;
        String accountNumber = null;

        while (accountNumberExists) {
            List<String> randomNumbers = new Random()
                    .ints(1, 1000000, 9999999)
                    .mapToObj(num -> String.format("%02d-%06d-%02d", num / 1000000, num % 1000000, num % 100))
                    .toList();
            accountNumber = randomNumbers.get(0);
            accountNumberExists = validateAccountNumber(accountNumber);
        }
        return accountNumber;
    }

    private boolean validateAccountNumber(String accountNumber) {
        return icesiAccountRepository.existsByAccountNumber(accountNumber);
    }
}
