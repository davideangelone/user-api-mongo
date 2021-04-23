package it.test.spring.jwt.mongodb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.test.spring.jwt.mongodb.handler.ResourceNotFoundException;
import it.test.spring.jwt.mongodb.models.dto.UserDto;
import it.test.spring.jwt.mongodb.repository.RoleRepository;
import it.test.spring.jwt.mongodb.repository.UserRepository;
import it.test.spring.jwt.mongodb.utils.BeanConverter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/{username}")
	public UserDto getUserById(@PathVariable("username") String username){
        return BeanConverter.convert(
        		userRepository.findByUsername(username).orElseThrow(() 
        				-> new ResourceNotFoundException()), UserDto.class);
    }
	
	@GetMapping("/email/{email}")
	public UserDto getUserByEmail(@PathVariable("email") String email){
        return BeanConverter.convert(
        		userRepository.findByEmail(email).orElseThrow(() 
        				-> new ResourceNotFoundException()), UserDto.class);

    }
	
	@GetMapping("/nationality/{nationality}")
	public List<UserDto> getUsersByNationality(@PathVariable("nationality") String nationality){
        return BeanConverter.convert(
        		userRepository.findByNationality(nationality), UserDto.class);

    }
	
	@GetMapping("/ages/{ageFrom}/{ageTo}")
	public List<UserDto> getUsersBetweenAges(@PathVariable("ageFrom") int ageFrom,
											 @PathVariable("ageTo") int ageTo){
        return BeanConverter.convert(
        		userRepository.findUsersByAgeBetween(ageFrom, ageTo), UserDto.class);

    }


}