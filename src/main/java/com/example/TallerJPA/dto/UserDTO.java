package com.example.TallerJPA.dto;

import com.example.TallerJPA.validations.ColombianPhone;
import com.example.TallerJPA.validations.NotNullContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NotNullContact.List({
    @NotNullContact(
            phone = "phoneNumber",
            email = "email",
            message = "Passwords do not match!"
    )
})
public class UserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @ColombianPhone
    private String phoneNumber;
    @NotBlank
    private String password;
    @NotBlank
    private String roleName;

}
