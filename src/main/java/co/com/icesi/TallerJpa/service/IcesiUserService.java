package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import co.com.icesi.TallerJpa.error.exception.DetailBuilder;
import co.com.icesi.TallerJpa.error.exception.ErrorCode;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapper;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.icesi.TallerJpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;

    public IcesiUserResponseDTO saveUser(IcesiUserRequestDTO icesiUserRequestDTO){
        List<String> errors = new ArrayList<>();
        if(icesiUserRepository.findByEmail(icesiUserRequestDTO.getEmail()).isPresent()){
            errors.add("email");
        }
        if(icesiUserRepository.findByPhoneNumber(icesiUserRequestDTO.getPhoneNumber()).isPresent()){
            errors.add("phoneNumber");
        }
        if(!errors.isEmpty()){
            throw createIcesiException(
                    "Field duplicated",
                    HttpStatus.BAD_REQUEST,
                    errors.stream()
                            .map(message -> new DetailBuilder(ErrorCode.ERR_400,message,"is duplicated"))
                            .toArray(DetailBuilder[]::new)
            ).get();
        }
        IcesiRole icesiRole = icesiRoleRepository.findByName(icesiUserRequestDTO.getRole())
                .orElseThrow(createIcesiException(
                        "Role doesn't exists",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404,"IcesiRole","name",icesiUserRequestDTO.getRole())
                ));
        IcesiUser icesiUser = icesiUserMapper.fromUserDto(icesiUserRequestDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(icesiRole);
        return icesiUserMapper.fromIcesiUserToResponse(icesiUserRepository.save(icesiUser));
    }

    public IcesiUserResponseDTO getUserById(UUID idString){
        return icesiUserMapper.fromIcesiUserToResponse(
                icesiUserRepository.findById(idString)
                        .orElseThrow(createIcesiException(
                                "User not found",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404,"IcesiUser","userId",idString)
                        ))
        );
    }

    public IcesiUserResponseDTO getUserByEmail(String email){
        return icesiUserMapper.fromIcesiUserToResponse(
                icesiUserRepository.findByEmail(email)
                        .orElseThrow(createIcesiException(
                                "User not found",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404,"IcesiUser","email",email)
                        ))
        );
    }
}
