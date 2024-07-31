package com.ilogistic.delivery_admin_backend.config
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    private val jwtSchemeName = "JWT AUTH"
    private val securityRequirement = SecurityRequirement().addList(jwtSchemeName)
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
            .components(Components()
                    .addSecuritySchemes(
                            jwtSchemeName,
                            SecurityScheme()
                                    .name(jwtSchemeName)
                                    .type(SecurityScheme.Type.HTTP) // HTTP 방식
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
            ))
            .addSecurityItem(securityRequirement)
            .info(apiInfo())

    private fun apiInfo() = Info()
            .title("아이화물 API")
            .description("아이화물 API 명세서")
            .version("1.0.0")
}
