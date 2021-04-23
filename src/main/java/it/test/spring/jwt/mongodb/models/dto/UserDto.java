package it.test.spring.jwt.mongodb.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Serializable {
	
	  private String username;
	
	  private String email;
	  
	  private String firstName;
	  
	  private String lastName;
	  
	  private String age;
	  
	  private String nationality;
	
	  public String getUsername() {
	    return username;
	  }
	
	  public void setUsername(String username) {
	    this.username = username;
	  }
	
	  public String getEmail() {
	    return email;
	  }
	
	  public void setEmail(String email) {
	    this.email = email;
	  }
	
	  public String getFirstName() {
		  return firstName;
	  }
	
	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }
	
	  public String getLastName() {
		  return lastName;
	  }
	
	  public void setLastName(String lastName) {
		  this.lastName = lastName;
	  }
	
	  public String getAge() {
		  return age;
	  }
	
	  public void setAge(String age) {
		  this.age = age;
	  }
	
	  public String getNationality() {
		  return nationality;
	  }
	
	  public void setNationality(String nationality) {
		  this.nationality = nationality;
	  }
}