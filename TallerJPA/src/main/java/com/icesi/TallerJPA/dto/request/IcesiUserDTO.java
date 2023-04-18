package com.icesi.TallerJPA.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserDTO {

    @NotBlank
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String rolName;
}
