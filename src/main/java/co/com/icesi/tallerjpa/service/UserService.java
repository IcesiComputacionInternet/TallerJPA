package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository UserRepository;
    private final UserMapper icesiUserMapper;

    public void save(UserDTO userDTO){
        UserRepository.save(icesiUserMapper.fromUserDTO(userDTO));


    }
}
