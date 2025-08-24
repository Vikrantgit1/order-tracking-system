package com.vg.orders.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Order Tracking System API")
                        .version("1.0.0")
                        .description("Microservices-based real-time order tracking platform")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Vikrant Goyal")
                                .email("vikrant051197@gmail.com")
                                .url("https://www.linkedin.com/in/vikrantgoyal/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}