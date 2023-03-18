package co.com.icesi.demojpa.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserCreateDTO {

    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String email;
    private String phone;
    private String password;
    private String roleName;



}
