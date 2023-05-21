package co.com.icesi.tallerjpa.integration.controller;

import co.com.icesi.tallerjpa.integration.configuration.TestConfigurationData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class ContextTest {
    @Test
    public void contextLoads() {
    }
}
