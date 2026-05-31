package com.splitbill.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI splitBillOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()

                                .title(
                                        "Split Bill Auth Service API"
                                )

                                .description(
                                        "Authentication API for Split Bill System"
                                )

                                .version("1.0.0")

                                .contact(
                                        new Contact()

                                                .name(
                                                        "Wahyu Aji Saputro"
                                                )

                                                .email(
                                                        "wahyu@mail.com"
                                                )
                                )
                );
    }
}

