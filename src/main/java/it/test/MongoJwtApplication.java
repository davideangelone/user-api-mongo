package it.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MongoJwtApplication {

	private static final Logger logger = LoggerFactory.getLogger(MongoJwtApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MongoJwtApplication.class, args);
		
		logger.info(">>> Json specs at : /v3/api-docs <<<");
		logger.info(">>> Yaml specs at : /v3/api-docs.yaml <<<");
		logger.info(">>> Swagger at : /swagger-ui.html <<<");	
	}

}
