package co.edu.icesi.tallerjpa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record LoginDTO(@NotNull(message = "The username can not be null")
                       @NotBlank(message = "The username can not be blank")
                       @NotEmpty(message = "The username can not be empty")
                       String username,
                       @NotNull(message = "The password can not be null")
                       @NotBlank(message = "The password can not be blank")
                       @NotEmpty(message = "The password can not be empty")
                       String password) {
}
