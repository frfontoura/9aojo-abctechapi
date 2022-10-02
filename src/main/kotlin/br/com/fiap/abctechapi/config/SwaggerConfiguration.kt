package br.com.fiap.abctechapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val SECURITY_SCHEME_NAME = "bearerAuth"

@Configuration
class SwaggerConfiguration(
    @Value("\${spring.profiles.active}")
    private val activeProfile: String
) {

    @Bean
    fun customizeOpenAPI(): OpenAPI? {
        return OpenAPI()
            .info(Info()
                .title("ABCTechAPI")
                .description("ABCTech API")
                .version("v0.0.1 - Profile: $activeProfile"))
            .addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEME_NAME, listOf("read", "write")))
            .components(Components().addSecuritySchemes(SECURITY_SCHEME_NAME, SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
    }

}