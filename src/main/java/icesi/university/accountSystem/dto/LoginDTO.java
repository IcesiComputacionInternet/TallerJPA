package icesi.university.accountSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//Record para crear clases que solo tienen atributos y getters
public record LoginDTO(@NotNull @NotBlank String username, @NotNull @NotBlank String password) {
}
