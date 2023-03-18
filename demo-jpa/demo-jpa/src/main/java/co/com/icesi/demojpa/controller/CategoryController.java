package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.repository.CategoryRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository cr){
        this.categoryRepository = cr;
    }
}
