package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public static String generateAccountNumber() {
        Random rand = new Random();
        String accountNumber = IntStream.generate(() -> rand.nextInt(10))
                .limit(10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(""));
        return accountNumber.replaceFirst("(\\d{3})(\\d{6})(\\d{2})", "$1-$2-$3");
    }


}
