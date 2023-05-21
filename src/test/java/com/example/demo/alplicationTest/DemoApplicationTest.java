package com.example.demo.alplicationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
public class DemoApplicationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testTokenEndPoint(){

    }
}
