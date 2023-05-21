package com.example.TallerJPA.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record LoginDTO (

        @NotBlank
        String username,

        @NotBlank
        String password){
}
