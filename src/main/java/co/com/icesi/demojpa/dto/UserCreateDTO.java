package co.com.icesi.demojpa.dto;


import co.com.icesi.demojpa.validate.email.EmailConstraint;
import co.com.icesi.demojpa.validate.phone.PhoneConstraint;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@Builder
public class UserCreateDTO {

    public UserCreateDTO() {
    }

    public UserCreateDTO(String firstName, String lastName, String email, String phone, String password, String roleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roleName = roleName;
    }
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

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
