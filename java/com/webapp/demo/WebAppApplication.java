package com.webapp.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class WebAppApplication {
	 private static final Logger LOGGER=LoggerFactory.getLogger(WebAppApplication.class);
	 
	public static void main(String[] args) {
		SpringApplication.run(WebAppApplication.class, args);
		LOGGER.info("********  Application Started  ********");
	}

}
