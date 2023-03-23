package co.com.icesi.demojpa.dto.response;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class ResponseUserDTO {

    private String firstName;

    private String lastname;

    private String email;

    private String phone;

    private String role;
}
