package co.com.icesi.demojpa.dto;


import co.com.icesi.demojpa.validate.email.EmailConstraint;
import co.com.icesi.demojpa.validate.phone.PhoneConstraint;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastname;

    @EmailConstraint
    @NotBlank
    private String email;

    @PhoneConstraint
    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotBlank
    private String roleName;

}
