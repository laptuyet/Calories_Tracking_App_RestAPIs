package com.caloriestracking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableList;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	@Value("${controller.package.name}")
	private String controllerPackage;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				// message chung cho toàn api đối với từng mã code
				.globalResponseMessage(RequestMethod.GET,
						ImmutableList.of(new ResponseMessageBuilder()
								.code(404).message("Not found").responseModel(new ModelRef("Error"))
								.code(400).message("Bad request").responseModel(new ModelRef("Error"))
								.code(403).message("Unauthenticated").responseModel(new ModelRef("Error"))
								.code(500).message("Internal server error").responseModel(new ModelRef("Error"))
								.build()))
				.select()
				.apis(RequestHandlerSelectors.basePackage(controllerPackage))
				.paths(PathSelectors.regex("/.*"))
				.build()
				.apiInfo(apiEndPointsInfo());

	}

	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder()
				.title("Calories Tracking App REST api :)")
				.description("Calories Tracking App Document for APIs")
				.contact(new Contact("Phi", "https://www.facebook.com/hoangphi.2104", "tranhoangphi0987@gmail.com"))
				.license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0.0")
				.build();
	}
}
