package com.icesi.TallerJPA.dto.request;


import com.icesi.TallerJPA.validation.emailphonevalue.interf.EmailOrPhoneValue;
import com.icesi.TallerJPA.validation.phonenumber.interf.ColombiaPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EmailOrPhoneValue
public class IcesiUserDTO {

    private String firstName;
    private String lastName;
    @Email
    private String email;
    @ColombiaPhoneNumber
    private String phoneNumber;
    private String password;
    private String rolName;
}
