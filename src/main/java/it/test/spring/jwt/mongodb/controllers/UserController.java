package it.test.spring.jwt.mongodb.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.test.spring.jwt.mongodb.handler.ResourceNotFoundException;
import it.test.spring.jwt.mongodb.models.ErrorJson;
import it.test.spring.jwt.mongodb.models.User;
import it.test.spring.jwt.mongodb.models.dto.UserDto;
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
	public UserDto getUserByUsername(@PathVariable("username") String username){
        return BeanConverter.convert(
        		userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new),
        	UserDto.class);
    }
	
	@GetMapping("/email/{email}")
	public UserDto getUserByEmail(@PathVariable("email") String email){
        return userRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);

    }
	
	@GetMapping("/nationality/{nationality}")
	public List<UserDto> getUsersByNationality(@PathVariable("nationality") String nationality){
        return userRepository.findByNationality(nationality);

    }
	
	@GetMapping("/ages/{ageFrom}/{ageTo}")
	public List<UserDto> getUsersBetweenAges(@PathVariable("ageFrom") int ageFrom,
											 @PathVariable("ageTo") int ageTo){
        return userRepository.findByAgeBetween(ageFrom, ageTo);

    }
	
	@PatchMapping("/update")
	public ResponseEntity<ErrorJson> updateUser(@Valid UserDto user) {
		
		User currentUser = userRepository
							.findByUsername(user.getUsername())
							.orElseThrow(ResourceNotFoundException::new);
			
		
		Optional<UserDto> userByEmail = userRepository.findByEmail(user.getEmail());
		if (userByEmail.isPresent() && !userByEmail.get().getUsername().equals(currentUser.getUsername())) {
			return new ResponseEntity<>(new ErrorJson(HttpStatus.BAD_REQUEST, "Email already in use"), HttpStatus.BAD_REQUEST);
		}
		
		BeanConverter.copyNonNullProperties(user, currentUser);
		
		userRepository.save(currentUser);
		
        return ResponseEntity.ok().build();
    }
	
	
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<ErrorJson> deleteUser(@PathVariable("username") String username) {
		if (!userRepository.existsByUsername(username)) {
			return new ResponseEntity<>(new ErrorJson(HttpStatus.BAD_REQUEST, "Username not found"), HttpStatus.BAD_REQUEST);
		}
		
		userRepository.deleteByUsername(username);
		
        return ResponseEntity.ok().build();
    }


}