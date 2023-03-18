package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapper;
import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;


    public IcesiAccount save(IcesiAccountDTO icesiAccountCreateDto) {

        icesiAccountCreateDto.setAccountNumber(generateAccountNumbers());

        if (icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber()).isPresent()) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_06.getMessage()));
        }

        if (accountBalance(icesiAccountCreateDto.getBalance())) {
            throw new  RuntimeException(String.valueOf(ErrorConstants.CODE_UD_14.getMessage()));
        }

        IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountDTO(icesiAccountCreateDto);
        icesiAccount.setAccountId(UUID.randomUUID());

        return icesiAccountRepository.save(icesiAccount);
    }

    private void validInformation(IcesiAccountDTO icesiAccountCreateDto) {
        if (icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber()).isPresent()) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_06.getMessage()));

        }

        if (accountBalance(icesiAccountCreateDto.getBalance())) {
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_07.getMessage()));
        }
    }


    public String generateAccountNumbers() {
        return generateNumberAccount(3).get() + "-" + generateNumberAccount(6).get() + "-" + generateNumberAccount(2).get();
    }

    public Supplier<String> generateNumberAccount(int length) {
        return () -> generateParts(length);
    }



    public boolean accountBalance(long balance) {
        if (0 > balance) {
            return true;
        } else {
            return false;
        }

    }

    public void modifyStateAccount(String numberAccount, IcesiAccountDTO icesiAccountDTO) {

        if (icesiAccountRepository.findByAccountNumber(numberAccount).isPresent()) {
            if (icesiAccountDTO.getBalance() == 0 && icesiAccountDTO.isActive()) {
                icesiAccountDTO.setActive(false);

            } else if (!icesiAccountDTO.isActive()) {
                icesiAccountDTO.setActive(true);

            } else {
                throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_09.getMessage()));
            }
        } else {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }

    }
    public String generateParts(int size) {

        String idAccount = "";
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            idAccount += random.nextInt(10);
        }

        return idAccount;
    }
    public void withdrawals(IcesiAccountDTO icesiAccountDTO,String numberAccount, int value) {
        if (icesiAccountRepository.findByAccountNumber(numberAccount).isPresent()) {
            long balance = icesiAccountDTO.getBalance();

            if (balance > value) {
                icesiAccountDTO.setBalance(balance - value);

            } else {

                throw new  RuntimeException(String.valueOf( ErrorConstants.CODE_UD_10.getMessage()));
            }
        } else {
            throw new  RuntimeException(String.valueOf( ErrorConstants.CODE_UD_08.getMessage()));
        }

    }

    public void depositCash(IcesiAccountDTO account, String numberAccount, int depositCash) {

        if (icesiAccountRepository.findByAccountNumber(numberAccount).isPresent() && depositCash > 0){
            account.setBalance(account.getBalance() + depositCash);

        }else if (depositCash <= 0){

            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_12.getMessage()));
        }
        else {

            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_08.getMessage()));
        }
    }

    public void sendMoney(IcesiAccountDTO sourceAccount, IcesiAccountDTO destinationAccount, int cashToTransfer) {

        if (icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber()).isPresent() &&
                icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber()).isPresent()) {


            if (validateAccountTypeOrigin(sourceAccount, destinationAccount)) {
                if (sourceAccount.getBalance() >= cashToTransfer) {
                    sourceAccount.setBalance(sourceAccount.getBalance() - cashToTransfer);
                    destinationAccount.setBalance(destinationAccount.getBalance() + cashToTransfer);

                } else if (cashToTransfer < 0) {
                    throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_12.getMessage()));

                } else {
                    throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_11.getMessage()));
                }
            }
        }else {
            validateAccountTypeOrigin(sourceAccount,destinationAccount);
        }
    }

    private boolean validateAccountTypeOrigin(IcesiAccountDTO icesiAccountCreateDto, IcesiAccountDTO destinationAccount) {
        boolean verificationOfType = true;

        if (icesiAccountCreateDto == null || destinationAccount == null) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }
        else if(icesiAccountCreateDto.getType().equalsIgnoreCase("Deposit Only") || destinationAccount.getType().equalsIgnoreCase("Deposit Only")){
            throw new  RuntimeException(String.valueOf( ErrorConstants.CODE_UD_13.getMessage()));
        }

        return verificationOfType;
    }

}
