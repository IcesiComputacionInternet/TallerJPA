package com.example.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    @Min(value = 0, message = "Balance must be greater than 0")
    @Max(value = 1000000000, message = "Balance must be less than 1000000000")
    private Long balance;

    private String type;

    @NotBlank
    private UserDTO user;
}


