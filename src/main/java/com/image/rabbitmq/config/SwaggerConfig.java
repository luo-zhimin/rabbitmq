package com.image.rabbitmq.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/7/13 21:57
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("rabbitMq")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("rabbitMq接口文档")
                .version("1.0")
                .build();
    }
}
