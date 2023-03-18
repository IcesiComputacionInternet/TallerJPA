package co.com.icesi.jpataller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IcesiUser {

    @Id
    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;
}
