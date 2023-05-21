package icesi.university.accountSystem.api;

import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface UserAPI {

    String BASE_USER_URL="/users";


    ResponseUserDTO getUser(@PathVariable String userEmail);


    List<ResponseUserDTO> getAllUsers();


    ResponseUserDTO addUser(RequestUserDTO requestUserDTO);

}
