package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.repository.BlogPostRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;

    public BlogPostController(BlogPostRepository bpr){
        this.blogPostRepository = bpr;
    }
}
