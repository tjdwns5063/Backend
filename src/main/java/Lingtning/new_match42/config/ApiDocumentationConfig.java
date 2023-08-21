package Lingtning.new_match42.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfig {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("New 매치42! API")
                        .description("New 매치42! API 입니다. 문의는 카톡이나 슬랙으로 연락주세요.")
                        .version("0.1.0")
                        .contact(new Contact()
                                .name("김민석")));
//                                .email("nicek789@gmail.com")));
//                .externalDocs(new ExternalDocumentation()
//                        .description("Documentation")
//                        .url("https:/wiki...."));
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("New Match42 API v1")
                .pathsToMatch(paths)
                .build();
    }
}
