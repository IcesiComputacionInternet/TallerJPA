package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import lombok.Data;

import javax.persistence.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class IcesiUserDto {

    private UUID userId;

    private String firstname;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private List<IcesiAccountDto> icesiAccounts;

    private IcesiRoleDto role;

}
