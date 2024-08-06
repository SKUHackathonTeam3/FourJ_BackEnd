package com.skuteam3.fourj.global.swagger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(title= "주적주적 API", description= "주적주적 백엔드 API Doc", version= "v1"),
        servers = {
                @Server(url = "http://localhost:8080", description = "주적주적 API LocalTest"),
                @Server(url = "http://ec2-43-201-61-252.ap-northeast-2.compute.amazonaws.com:8080", description = "주적주적 API Develop"),
                @Server(url = "https://api.smartcheers.site", description = "주적주적 API Production")
        }
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    protected SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()));
    }
}
