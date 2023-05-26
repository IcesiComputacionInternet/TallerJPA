package co.com.icesi.jpataller.dto;

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
    @NotBlank
    private String email;

    // Requires extra Phone Constraint
    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    private String roleName;
}
