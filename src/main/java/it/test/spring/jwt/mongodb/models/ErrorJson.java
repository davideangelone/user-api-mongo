package it.test.spring.jwt.mongodb.models;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class ErrorJson {

    private Integer status;
    private String 	error;

    public ErrorJson(int status, Map<String, Object> errorAttributes) {
        this.status = status;
        this.error = (String) errorAttributes.get("error");
    }
    
    public ErrorJson(HttpStatus status) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
    }
    
    public ErrorJson(HttpStatus status, String error) {
        this.status = status.value();
        this.error = error;
    }

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

}