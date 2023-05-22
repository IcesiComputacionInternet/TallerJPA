package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.validations.cellphonenumber.ColombianPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserRequestDTO {
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    private String firstName;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    private String lastName;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    @Email
    private String email;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    @ColombianPhoneNumber
    private String phoneNumber;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    private String password;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    private String role;
}
