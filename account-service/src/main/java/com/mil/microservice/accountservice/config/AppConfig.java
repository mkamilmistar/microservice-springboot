package com.mil.microservice.accountservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableFeignClients("com.mil.microservice.accountservice.feignclient")
public class AppConfig {

}
