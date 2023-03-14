package com.icesi.TallerJPA.dto;


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
    private String phoneNumber;
    private String password;
}
