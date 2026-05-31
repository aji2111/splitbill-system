package com.splitbill.expense_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI expenseOpenAPI() {

        final String securitySchemeName =
                "bearerAuth";

        return new OpenAPI()

                .info(
                        new Info()

                                .title(
                                        "Split Bill Expense Service API"
                                )

                                .description(
                                        "Expense API for Split Bill System"
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
                )

                .addSecurityItem(

                        new SecurityRequirement()

                                .addList(
                                        securitySchemeName
                                )
                )

                .schemaRequirement(

                        securitySchemeName,

                        new SecurityScheme()

                                .name(
                                        securitySchemeName
                                )

                                .type(
                                        SecurityScheme.Type.HTTP
                                )

                                .scheme("bearer")

                                .bearerFormat("JWT")
                );
    }
}

