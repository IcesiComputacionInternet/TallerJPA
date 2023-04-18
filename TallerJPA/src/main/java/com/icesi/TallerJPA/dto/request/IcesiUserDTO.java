package com.icesi.TallerJPA.dto.request;


import com.icesi.TallerJPA.validation.notation.ValidatePhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    @ValidatePhoneNumber
    private String phoneNumber;
    private String password;
    private String rolName;
}
