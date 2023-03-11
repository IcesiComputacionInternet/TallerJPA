package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository repository;
    private final IcesiUserMapper mapper;

    //TODO: implement all the necessary methods
}
