package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
}
