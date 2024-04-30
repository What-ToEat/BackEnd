package capstone.restaurant;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "뭐먹을까 API Docs",
                description = "뭐먹을까의 API 문서이다.",
                version = "1.0"
        )
)
@Configuration
public class SwaggerConfig {
}
