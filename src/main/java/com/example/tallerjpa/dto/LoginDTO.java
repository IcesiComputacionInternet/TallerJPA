package com.example.tallerjpa.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

        @NotBlank
        String username;

        @NotBlank
        String password;

}

