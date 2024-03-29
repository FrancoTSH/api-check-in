package com.renatofranco.bsalecheckin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI getApiInfo() {
    return new OpenAPI().info(new Info()
        .title("Check-In API")
        .version("1.0.0")
        .description("API que nos permite hacer un check-in automatico a los pasajeros de una aerolinea.")
        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0")));
  }
}
