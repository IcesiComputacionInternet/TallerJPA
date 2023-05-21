package com.example.tallerjpa.dto;

import com.example.tallerjpa.validation.phonenumber.interfaces.ColombianNumber;
import lombok.*;

import javax.validation.constraints.Email;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {


    private String firstName;

    private String lastName;
    @Email(message = "the email must have a correct structure")
    private String email;
    @ColombianNumber
    private String phoneNumber;

    private String password;

    private String role;
}
