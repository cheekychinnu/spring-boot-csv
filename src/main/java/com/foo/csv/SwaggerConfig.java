package com.foo.csv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.foo.csv"))
                .paths(regex("/rest.*"))
                .build()
                .apiInfo(getApiInfo());

    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Vinodhini", "somedummyurl.com", "someuser@gmail.com");
        return new ApiInfoBuilder().description("Spring Boot CSV read-write example").title("Spring Boot - CSV").contact(contact).build();
    }
}
