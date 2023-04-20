package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.validations.cellphonenumber.ColombianPhoneNumber;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    @ColombianPhoneNumber
    private String phoneNumber;
    private String password;
}
