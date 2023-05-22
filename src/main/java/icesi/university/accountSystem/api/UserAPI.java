package icesi.university.accountSystem.api;


import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserAPI {

    String BASE_USER_URL="/users";


    ResponseUserDTO getUser(@PathVariable String userEmail);


    List<ResponseUserDTO> getAllUsers();


    ResponseUserDTO addUser(RequestUserDTO requestUserDTO);

    ResponseUserDTO assignRole(String userEmail,String roleName);



}
