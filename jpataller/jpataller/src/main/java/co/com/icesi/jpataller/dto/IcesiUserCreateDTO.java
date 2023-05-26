package co.com.icesi.jpataller.dto;

import co.com.icesi.jpataller.validations.email.EmailConstraint;
import co.com.icesi.jpataller.validations.phone.PhoneConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    // Requires extra Email Constraint
    @EmailConstraint
    @NotBlank
    private String email;

    // Requires extra Phone Constraint
    @PhoneConstraint
    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    private String roleName;
}
