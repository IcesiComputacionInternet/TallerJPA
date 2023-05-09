package co.com.icesi.demojpa.dto.response;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.List;


@Data
@Builder
public class ResponseUserDTO {


    private String firstName;

    private String lastname;

    private String email;

    private String phone;

    private IcesiRole role;
}
