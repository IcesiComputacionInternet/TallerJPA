package co.edu.icesi.tallerjpa.runableartefact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiPermission {

    @Id
    @GeneratedValue(generator = "uuid")
    private Long id;

    @NotBlank
    private String key;

    @NotBlank
    private String path;

}
