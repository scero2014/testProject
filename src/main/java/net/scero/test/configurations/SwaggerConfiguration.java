package net.scero.test.configurations;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * SwaggerConfiguration
 * 
 * @author mhiginio
 */
@Configuration
public class SwaggerConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private TypeResolver typeResolver;
    
    /**
     * Constructs Docket
     * 
     * @return Docket
     */
    @Bean
    public Docket docketFactory() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(metadata())
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(LocalDateTime.class, String.class)
            .directModelSubstitute(LocalTime.class, String.class)
            .directModelSubstitute(OffsetDateTime.class, String.class)
            .directModelSubstitute(OffsetTime.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .alternateTypeRules(
                newRule(typeResolver.resolve(DeferredResult.class,
                    typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                    typeResolver.resolve(WildcardType.class)))
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("net.scero.test.controllers.swagger"))
            .paths(PathSelectors.any())
            .build();
    }

    /**
     * @return UIConfiguration
     */
    @Bean
    public UiConfiguration swaggerUiConfig() {
        return new UiConfiguration(null);
    }

    /**
     * Return metadata
     * 
     * @return ApiInfo
     */
    @Bean
    public ApiInfo metadata() {
        return new ApiInfoBuilder()
            .title("test-swagger-ws")  // Service name
            .description("Test swagger service")  // Service description
            .version("1.0")
            .contact(new Contact("by Scero", "http://scero.net", "scero@scero.net"))
            .build();
    }
    
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//            .addResourceHandler("swagger-ui.html")
//            .addResourceLocations("classpath:/META-INF/resources/");
//        registry
//            .addResourceHandler("/webjars/**")
//            .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}
