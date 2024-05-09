package br.usp.websemantica.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
   
	@Bean
    public Docket productApi() { 
		List<SecurityScheme> schemeList = new ArrayList<>();
    	//schemeList.add(new BasicAuth("basicAuth"));
    	schemeList.add(new ApiKey("apiKeyAuth", "api_key", "query")); 
    	
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("br.usp.websemantica.layer.api"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(metaData())
                .securitySchemes(schemeList);             
    }
        
    private ApiInfo metaData() {    	
        return new ApiInfoBuilder()
                .title("SISTB - API Semântica")
                .description("")
                .version("1.0")
                //.license("Apache License Version 2.0")
                //.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")                
                //.contact(new Contact("John Thompson", "https://springframework.guru/about/", "john@springfrmework.guru"))
                .build();
    }
    
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
 
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
