package br.com.garage.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
info = @Info(description = "OpenApi documentation para o webservice Auth-api",
		title = "Auth-api - documentação", version = "1.0"),
servers = {
		@Server(description = "Developer", url = "http://localhost:8080"),
		@Server(description = "Staginig", url = "http://192.168.0.237") },
security = { @SecurityRequirement(name = "bearerAuth") })
@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
