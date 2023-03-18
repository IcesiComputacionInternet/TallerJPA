package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.repository.CatergoryRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private final CatergoryRepository catergoryRepository;

    public CategoryController(CatergoryRepository catergoryRepository) {
        this.catergoryRepository = catergoryRepository;
    }
}
