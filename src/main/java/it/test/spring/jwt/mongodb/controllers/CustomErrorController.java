package it.test.spring.jwt.mongodb.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import it.test.spring.jwt.mongodb.models.ErrorJson;

@RestController
public class CustomErrorController implements ErrorController {
	
	private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    ErrorJson error(HttpServletRequest request, HttpServletResponse response) {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring. 
        // Here we just define response body.
    	return new ErrorJson(response.getStatus(), errorAttributes.getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults()));
    }

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
