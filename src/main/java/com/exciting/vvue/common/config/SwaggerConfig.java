package com.exciting.vvue.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "bearerAuth";
    private static final String SECURITY_SCHEME_BEARER = "bearer";
    private static final String SECURITY_SCHEME_HTTP = "HTTP";
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";

    @Value("${swagger.url}")
    private String serverUrl;

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
            .group("vvue apis")
            .packagesToScan("com.exciting.vvue")
            .packagesToExclude("com.exciting.vvue.develop")
            .build();
    }

    @Bean
    public GroupedOpenApi developApiGroup() {
        return GroupedOpenApi.builder()
            .group("develop apis")
            .packagesToScan("com.exciting.vvue.develop")
            .build();
    }

    @Bean
    public OpenAPI springOpenApi() {
        return new OpenAPI()
            .components(createComponents())
            .security(Collections.singletonList(createSecurityRequirement()))
            .info(createApiInfo())
            .addServersItem(createServer());
    }

    private Components createComponents() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.valueOf(SECURITY_SCHEME_HTTP))
            .scheme(SECURITY_SCHEME_BEARER)
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(HEADER_NAME_AUTHORIZATION);
        return new Components().addSecuritySchemes(BEARER_AUTH, securityScheme);
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(BEARER_AUTH);
    }

    private Info createApiInfo() {
        return new Info()
            .title("vvue REST API")
            .version("1.0.0")
            .description("vvue swagger api 입니다.");
    }

    private Server createServer() {
        return new Server().url(serverUrl);
    }
}
