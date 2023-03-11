package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class IcesiAccountController {
    private final IcesiAccountService service;
}
