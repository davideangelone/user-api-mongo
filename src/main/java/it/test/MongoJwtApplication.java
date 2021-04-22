package it.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoJwtApplication {

	private static final Logger logger = LoggerFactory.getLogger(MongoJwtApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MongoJwtApplication.class, args);
		
		logger.info(">>> Docs at : /v3/api-docs <<<");
		logger.info(">>> Swagger at : /swagger-ui.html <<<");	
	}

}
