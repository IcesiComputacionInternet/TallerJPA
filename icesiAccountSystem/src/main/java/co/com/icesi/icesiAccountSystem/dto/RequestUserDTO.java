package co.com.icesi.icesiAccountSystem.dto;

import co.com.icesi.icesiAccountSystem.validation.interfaces.EmailAndPhoneConstraint;
import co.com.icesi.icesiAccountSystem.validation.interfaces.PhoneNumberConstraint;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EmailAndPhoneConstraint
public class RequestUserDTO {
    @NotNull(message = "The role of a user can't be null")
    @NotBlank(message = "The role of a user is mandatory")
    private String roleName;
    @NotBlank(message = "The first name of a user is mandatory")
    private String firstName;
    @NotBlank(message = "The lastname of a user is mandatory")
    private String lastName;
    @NotNull(message = "The email of a user can't be null")
    @Email(message = "Invalid email address")
    private String email;
    @NotNull(message = "The phone number of a user can't be null")
    @PhoneNumberConstraint
    private String phoneNumber;
    @NotBlank(message = "The password of a user is mandatory")
    @NotNull(message = "The password of a user can't be null")
    private String password;
}
