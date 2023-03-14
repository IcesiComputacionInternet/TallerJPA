package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


}
