package space.atnibam.common.swagger.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.atnibam.common.swagger.constant.SwaggerConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 启用OpenAPI注解
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    /**
     * 生成SwaggerProperties的Bean
     *
     * @return SwaggerProperties的Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    /**
     * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     *
     * @param swaggerProperties SwaggerProperties对象
     * @return Docket对象
     */
    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {

        // base-package处理
        if (swaggerProperties.getBasePackage().isEmpty()) {
            swaggerProperties.setBasePackage(SwaggerConstants.BASE_PACKAGE);
        }

        // base-path处理
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(SwaggerConstants.DEFAULT_BASE_PATH);
        }
        // noinspection unchecked
        List<Predicate<String>> basePath = new ArrayList<>();
        swaggerProperties.getBasePath().forEach(path -> basePath.add(PathSelectors.ant(path)));

        // exclude-path处理
        if (swaggerProperties.getExcludePath().isEmpty()) {
            swaggerProperties.getExcludePath().addAll(SwaggerConstants.DEFAULT_EXCLUDE_PATH);
        }
        List<Predicate<String>> excludePath = new ArrayList<>();
        swaggerProperties.getExcludePath().forEach(path -> excludePath.add(PathSelectors.ant(path)));


        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerProperties.getEnabled())
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                //此包路径下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                //加了Api注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class));

        swaggerProperties.getBasePath().forEach(p -> builder.paths(PathSelectors.ant(p)));
        swaggerProperties.getExcludePath().forEach(p -> builder.paths(PathSelectors.ant(p).negate()));

        return builder
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping("/");
    }


    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     *
     * @return List<SecurityScheme>对象
     */
    private List<SecurityScheme> securitySchemes() {
        SecurityScheme scheme = new ApiKey(SwaggerConstants.ACCESS_TOKEN_KEY, SwaggerConstants.ACCESS_TOKEN_KEY, "header");
        return Collections.singletonList(scheme);
    }

    /**
     * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
     *
     * @return List<SecurityContext>对象
     */
    private List<SecurityContext> securityContexts() {
        SecurityContext context = SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
        return Collections.singletonList(context);
    }

    /**
     * 默认的全局鉴权策略
     *
     * @return List<SecurityReference>对象
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference(SwaggerConstants.ACCESS_TOKEN_KEY, authorizationScopes));
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @param swaggerProperties SwaggerProperties对象
     * @return ApiInfo对象
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }
}