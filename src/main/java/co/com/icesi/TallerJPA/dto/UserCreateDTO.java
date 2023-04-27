package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.validation.validateEmailorPhone.EmailOrPhoneConstraint;
import co.com.icesi.TallerJPA.validation.validatePhoneNumber.ColombianNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
//@EmailOrPhoneConstraint
public class UserCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotNull
    private String email;

    //@ColombianNumberConstraint
    @NotNull
    private String phoneNumber;

    @NotNull
    private String password;

    @NotNull
    private String role;
}
