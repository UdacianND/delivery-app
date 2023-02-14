package uz.md.shopapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        name = "token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        Contact contact = new Contact();
        contact.setName("Muhammadqodir Yusupov");
        return new OpenAPI()
                .info(new Info()
                        .title("Rest full Apis of Shop app ")
                        .version("1.0.0")
                        .description(" Shop App ")
                        .contact(contact)
                );
    }

}