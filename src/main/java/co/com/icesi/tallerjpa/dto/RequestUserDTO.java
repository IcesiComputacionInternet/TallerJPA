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
@AllArgsConstructor
public class RequestUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String password;
    @NotNull
    @ColombianNumber
    private String phoneNumber;
    @NotBlank
    private String role;
}
