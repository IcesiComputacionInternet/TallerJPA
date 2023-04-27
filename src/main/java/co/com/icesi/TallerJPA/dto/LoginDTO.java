package co.com.icesi.TallerJPA.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record LoginDTO(

        @NotNull
        @NotBlank
        String username,
        @NotNull
        @NotBlank

        String password) {
}
