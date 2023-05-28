package co.com.icesi.TallerJpa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record LoginDTO (
        @NotNull(message = "can't be null.")
        @NotBlank(message = "can't be blank.")
        @NotEmpty(message = "can't be empty.")
        @Email
        String username,
        @NotNull(message = "can't be null.")
        @NotBlank(message = "can't be blank.")
        @NotEmpty(message = "can't be empty.")
        String password) {
}
