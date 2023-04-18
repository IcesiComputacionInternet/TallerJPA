package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.RoleAPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.example.TallerJPA.api.RoleAPI.BASE_ROLE_URL;

@AllArgsConstructor
@RestController(BASE_ROLE_URL)
public class RoleController implements RoleAPI {

}
