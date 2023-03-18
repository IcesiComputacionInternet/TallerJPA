package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.IcesiUserDTO;
import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    public IcesiUser save(IcesiUserDTO user){
        Optional<IcesiRole> role = Optional.of(user.getRole());
        Optional<IcesiUser> userByPhoneNumber = icesiUserRepository.findByPhoneNumber(user.getPhoneNumber());
        Optional<IcesiUser> userByEmail = icesiUserRepository.findByEmail(user.getEmail());
        if(role.isPresent()) {
            if (userByPhoneNumber.isPresent() && userByEmail.isPresent()) {
                throw new RuntimeException("email and phoneNumber already in use");
            } else if (userByEmail.isPresent()) {
                throw new RuntimeException("email already in use");
            } else if (userByPhoneNumber.isPresent()) {
                throw new RuntimeException("phoneNumber already in use");
            }
        }

        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        return icesiUserRepository.save(icesiUser);
    }

}
