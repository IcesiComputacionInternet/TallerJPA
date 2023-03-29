package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.validation.phone_number.interfaces.ColombianNumber;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class RequestUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @ColombianNumber
    private String phoneNumber;
    @NotNull
    private String role;
}
