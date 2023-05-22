package co.com.icesi.TallerJpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record LoginDTO (
        @NotNull(message = "can't be null.")
        @NotBlank(message = "can't be blank.")
        String username,
        @NotNull(message = "can't be null.")
        @NotBlank(message = "can't be blank.")
        String password) {
}
