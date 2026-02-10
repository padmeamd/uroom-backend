package com.uroom.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
        info = @Info(
                title = "Uroom API",
                version = "1.0",
                description = "Backend API for room matching platform"
        )
)
@SpringBootApplication
public class UroomBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(UroomBackendApplication.class, args);
    }
}
