package com.leettracker.leettrack.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LeetTracker REST API",
                version = "1.0.0",
                description = "A secure REST API for tracking solved LeetCode problems, built with Spring Boot, Spring Security, JWT Authentication, and MySQL.",
                contact = @Contact(
                        name = "Karan Kumar",
                        email = "hellokaran7466@gmail.com"
                ),
                license = @License(
                        name = "MIT License"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}