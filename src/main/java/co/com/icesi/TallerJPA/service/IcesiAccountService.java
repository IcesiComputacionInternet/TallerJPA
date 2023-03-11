package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private  final IcesiAccountMapper mapper;
    private final IcesiAccountRespository respository;
    //TODO: implement
}
