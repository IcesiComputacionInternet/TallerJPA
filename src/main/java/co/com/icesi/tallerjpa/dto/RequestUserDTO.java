package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.validation.email_or_phone.interfaces.EmailOrPhoneNumber;
import co.com.icesi.tallerjpa.validation.phone_number.interfaces.ColombianNumber;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@EmailOrPhoneNumber
public class RequestUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @ColombianNumber
    private String phoneNumber;
    @NotNull
    private String role;
}
