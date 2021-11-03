package nl.rabobank;

import nl.rabobank.mapper.PowerOfAttorneyMapper;
import nl.rabobank.mongo.MongoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Import(MongoConfiguration.class)
@EnableSwagger2
public class RaboAssignmentApplication {
    public static void main(final String[] args) {
        SpringApplication.run(RaboAssignmentApplication.class, args);
    }

    @Bean
    public PowerOfAttorneyMapper powerOfAttorneyMapper() {
        return new PowerOfAttorneyMapper();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("nl.rabobank.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
