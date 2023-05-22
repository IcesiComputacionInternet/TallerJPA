package co.com.icesi.TallerJpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleChangeDTO {
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    @Email
    String email;
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    String role;
}
