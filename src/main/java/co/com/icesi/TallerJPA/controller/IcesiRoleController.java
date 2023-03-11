package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class IcesiRoleController {
    private final IcesiRoleService service;
    //TODO: Implement the necessary enpoints
}
