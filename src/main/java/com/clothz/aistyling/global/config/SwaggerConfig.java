package com.clothz.aistyling.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_KEY = "bearer-key";
    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";
    private static final String AUTHORIZATION = "Authorization";
    private static final String TITLE = "AI Styling";
    private static final String DESCRIPTION = "AI Styling 프로젝트 Swagger UI";
    private static final String VERSION = "0.0.1";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BEARER_KEY))
                .components(new Components()
                        .addSecuritySchemes(BEARER_KEY, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(BEARER)
                                .bearerFormat(JWT)
                                .in(SecurityScheme.In.HEADER)
                                .name(AUTHORIZATION)))
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION))
                .addServersItem(new Server().url("/"));
    }

}
