package com.platform.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${server.servlet.context-path}") String contextPath,
        @Value("${application.description}") String description,
        @Value("${application.version}") String version,
        @Value("${build.date}") String buildDate){
        OpenAPI openAPI = new OpenAPI().info(new Info().title(description).version(version)
                .description(description.concat("[".concat(buildDate).concat("]")))
                        .termsOfService("http://swagger.io.terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
        return openAPI;
    }

    @Bean
    public GroupedOpenApi publicAPI(){
        return GroupedOpenApi.builder()
                .group("Publish")
                .packagesToScan("com.platform.controller.publish")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi actuatorAPI(){
        return GroupedOpenApi.builder()
                .group("Internal")
                .packagesToScan("com.platform.controller.internal")
                .pathsToMatch("/**")
                .build();
    }
}
