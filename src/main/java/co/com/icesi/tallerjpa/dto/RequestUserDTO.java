package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.constraint.CellphoneConstraint;
import co.com.icesi.tallerjpa.constraint.EmailorPhoneConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EmailorPhoneConstraint
public class RequestUserDTO {
    @NotBlank(message = "First name may not be blank")
    private String firstName;
    @NotBlank(message = "Lastname may not be blank")
    private String lastName;
    @Email(message = "Invalid email")
    @NotBlank(message = "Email may not be blank")
    private String email;
    @NotNull(message = "Password may not be null")
    @NotBlank(message = "Password may not be blank")
    private String password;
    @CellphoneConstraint
    private String phoneNumber;
    @NotBlank(message = "Role may not be blank")
    @NotNull(message = "Role may not be null")
    private String role;
}