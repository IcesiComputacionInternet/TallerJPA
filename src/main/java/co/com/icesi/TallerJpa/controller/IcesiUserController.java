package co.com.icesi.TallerJpa.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiUserApi;
import co.com.icesi.TallerJpa.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiUserController implements IcesiUserApi {

    private IcesiUserService icesiUserService;
}
