package co.com.icesi.TallerJpa.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = IcesiUserApi.USER_BASE_URL)
public interface IcesiUserApi {

    String USER_BASE_URL = "/api/user";

}
