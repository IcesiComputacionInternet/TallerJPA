package co.edu.icesi.demo.service;

import co.edu.icesi.demo.mapper.IcesiUserMapper;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;


}
