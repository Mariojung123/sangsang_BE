package com.example.SecureAndBox.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
	info = @Info(title = "SecureAndBox",
		description = "SecureAndBox",

		version = "v1"))
@RequiredArgsConstructor
@SecuritySchemes({
	@SecurityScheme(
		name = "JWT Authorization",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "Bearer"
	)
})
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		io.swagger.v3.oas.models.security.SecurityScheme securityScheme = new io.swagger.v3.oas.models.security.SecurityScheme()
			.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
			.name("Authorization");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT Authorization");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("JWT Authorization", securityScheme))
			.security(Arrays.asList(securityRequirement));
	}
}