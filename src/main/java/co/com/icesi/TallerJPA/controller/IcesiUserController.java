package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class IcesiUserController {
    private final IcesiUserService service;
    //TODO: implement necessary endpoints
}
