package com.example.tallerjpa.dto;


import lombok.NonNull;

import javax.validation.constraints.NotBlank;

public record LoginDTO (

        @NonNull
        @NotBlank
        String username,
        @NonNull
        @NotBlank
        String password){
}
