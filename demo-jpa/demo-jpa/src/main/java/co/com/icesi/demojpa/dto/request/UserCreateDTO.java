package co.com.icesi.demojpa.dto.request;


import co.com.icesi.demojpa.validation.emailphonevalue.interf.EmailOrPhoneValue;
import co.com.icesi.demojpa.validation.phonenumber.interf.ColombiaPhoneNumber;
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
public class UserCreateDTO {

    private String firstName;
    private String lastName;
    @Email
    private String email;
    @ColombiaPhoneNumber
    private String phone;
    private String password;
    private String roleName;



}
