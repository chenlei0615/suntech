package com.suntech.feo.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:00
 * ------------    --------------    ---------------------------------
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig{
    @Value("${swagger.enable}")
    private boolean enableSwagger = true;

    @Value("${spring.profiles.active}")
    private String env ;

    @Bean
    public Docket createAPI() {
//        String path = env.equals("local") ? "/**" : "/api/v1";
// jwt验证解决
        ParameterBuilder pb = new ParameterBuilder();
        List<Parameter> token = new ArrayList<>();
        pb.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        token.add(pb.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.suntech.feo.controller"))
//                .apis(RequestHandlerSelectors.any())
                //过滤生成链接
                .paths(PathSelectors.any())
                .build()
//                .pathMapping(path)
                .apiInfo(apiInfo())
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(securityContexts());
//                .globalOperationParameters(token);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("量资云码系统API文档")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
}
