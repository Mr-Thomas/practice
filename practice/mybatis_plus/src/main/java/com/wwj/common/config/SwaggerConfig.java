package com.wwj.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {
	
	/**
	 * 是否开启接口文档
	 */
	@Value("${swagger.enable}")
	private boolean enableSwagger;
	@Value("${swagger.server.host}")
	private String host;
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tj.practice"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
				.enable(enableSwagger)
				;
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Spring-Boot基础API文档")
                .description("武汉天浚网络科技有限公司版权所有")
                .termsOfServiceUrl(host)
				.version("1.0.0")
				.build()
				;
	}
	
}
