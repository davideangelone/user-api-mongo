package it.test.spring.jwt.mongodb.models;

import java.util.Map;

public class ErrorJson {

    public Integer 	status;
    public String 	error;

    public ErrorJson(int status, Map<String, Object> errorAttributes) {
        this.status = status;
        this.error = (String) errorAttributes.get("error");
    }

}