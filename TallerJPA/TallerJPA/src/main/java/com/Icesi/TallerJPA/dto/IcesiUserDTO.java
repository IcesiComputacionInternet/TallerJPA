package com.Icesi.TallerJPA.dto;

import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IcesiUserDTO {

    private long balance;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<IcesiAccount> accounts;
    private IcesiRole icesiRole;
}
