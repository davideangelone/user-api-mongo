package it.test.spring.jwt.mongodb.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingHandler {

    private static final Logger log = LoggerFactory.getLogger(LoggingHandler.class);

    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void repository() {}
    
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() { }
    
    private String serialize(Object ojb) {
    	try {
    		return new ObjectMapper().writeValueAsString(ojb);
    	} catch (Exception e) {
    		return String.valueOf(ojb);
    	}
    }

    //before -> Any resource annotated with @Repository annotation
    //and all method and function
    @Before("(repository() || controller()) && allMethod()")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        log.info(">>>> {} - [{}.{}] - Method [{}] - Args {}",
        		new SimpleDateFormat("dd-MM-yyyy HH24:mm:ss").format(new Date()),
        		joinPoint.getSignature().getDeclaringType().getSimpleName(),
        		joinPoint.getSignature().getName(),
        		joinPoint.getSignature(), 
        		serialize(joinPoint.getArgs()));
    }

    //After -> All method within resource annotated with @Repository annotation
    @AfterReturning(pointcut = "(repository() || controller()) && allMethod()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        log.info("<<<< {} - [{}.{}] - Method [{}] - Result [{}]",
        		new SimpleDateFormat("dd-MM-yyyy HH24:mm:ss").format(new Date()),
        		joinPoint.getSignature().getDeclaringType().getSimpleName(),
        		joinPoint.getSignature().getName(),
        		joinPoint.getSignature(),
        		serialize(result));
    }
}