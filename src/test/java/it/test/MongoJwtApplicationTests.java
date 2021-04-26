package it.test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import it.test.spring.jwt.mongodb.controllers.AuthController;
import it.test.spring.jwt.mongodb.controllers.UserController;
import it.test.spring.jwt.mongodb.models.ERole;
import it.test.spring.jwt.mongodb.models.Role;
import it.test.spring.jwt.mongodb.models.dto.UserDto;
import it.test.spring.jwt.mongodb.payload.request.LoginRequest;
import it.test.spring.jwt.mongodb.payload.request.SignupRequest;
import it.test.spring.jwt.mongodb.repository.RoleRepository;
import it.test.spring.jwt.mongodb.repository.UserRepository;

@SpringBootTest
//@DataMongoTest
class MongoJwtApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(MongoJwtApplicationTests.class);
    
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthController authController;
	
	@Autowired
	UserController userController;
	
		
	@BeforeEach
    public void initDatabase() {
		 logger.info("Begin initDatabase");
		 
		 Set<Role> repositoryRoles = Stream.of(new Role(ERole.ROLE_ADMIN), new Role(ERole.ROLE_MODERATOR), new Role(ERole.ROLE_USER))
				  .collect(Collectors.toSet());
		 roleRepository.saveAll(repositoryRoles);
		 
		 SignupRequest user = new SignupRequest();
		 user.setUsername("testUser1");
		 user.setPassword("testPassword1");
		 user.setEmail("testUser1@test.it");
		
		 Set<String> roles = Stream.of(ERole.ROLE_ADMIN.toString(),
									  ERole.ROLE_MODERATOR.toString(),
									  ERole.ROLE_USER.toString())
				  .collect(Collectors.toSet());
		 
		 user.setRole(roles);
		
		 ResponseEntity<?> result = authController.registerUser(user);
		 assertEquals(HttpStatus.OK, result.getStatusCode());
		  
		 logger.info("End initDatabase");
	}
	
	@AfterEach
	public void clearDatabase() {
		logger.info("Begin clearDatabase");
		roleRepository.deleteAll();
		userRepository.deleteAll();
		logger.info("End clearDatabase");
	}
	  
	

	@Test
	void testRoles() {
		logger.info("Begin Test roles");
		assertEquals(3, this.roleRepository.findAll().size());
		logger.info("End Test roles");
	}
	
	@Test
	void testAddUser() {
		logger.info("Begin Test update user");
		
		UserDto user = userController.getUserByUsername("testUser1");
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setNationality("italian");
		user.setAge(50);
		
		ResponseEntity<?> result = userController.updateUser(user);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
		logger.info("End Test update user");
	}
	
	@Test
	void testRegisterUser1() {
		logger.info("Begin Test register user 1");
		
		SignupRequest user = new SignupRequest();
		user.setUsername("testUser1");
		user.setPassword("testPassword1");
		user.setEmail("testUser1@test.it");
		
		Set<String> roles = Stream.of(ERole.ROLE_ADMIN.toString(),
									  ERole.ROLE_MODERATOR.toString(),
									  ERole.ROLE_USER.toString())
				  .collect(Collectors.toSet());
		 
		user.setRole(roles);
		
	    ResponseEntity<?> result = authController.registerUser(user);
	    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		  
		logger.info("End Test register user 1");
	}
	
	
	@Test
	void testRegisterUser2() {
		logger.info("Begin Test register user 2");
		
		SignupRequest user = new SignupRequest();
		user.setUsername("testUser2");
		user.setPassword("testPassword2");
		user.setEmail("testUser2@test.it");
		
		Set<String> roles = Stream.of(ERole.ROLE_ADMIN.toString(),
									  ERole.ROLE_MODERATOR.toString(),
									  ERole.ROLE_USER.toString())
				  .collect(Collectors.toSet());
		 
		user.setRole(roles);
		
	    ResponseEntity<?> result = authController.registerUser(user);
	    assertEquals(HttpStatus.OK, result.getStatusCode());
		  
		logger.info("End Test register user 2");
	}
	
	@Test
	void testLoginUser1() {
		logger.info("Begin Test login user 1");
		
		LoginRequest user = new LoginRequest();
		user.setUsername("testUser2");
		user.setPassword("testPassword2");
		
	    assertThatExceptionOfType(BadCredentialsException.class)
	    	.isThrownBy(() -> authController.authenticateUser(user));
		  
		logger.info("End Test login user 1");
	}
	
	
	@Test
	void testLoginUser2() {
		logger.info("Begin Test login user 2");
		
		LoginRequest user = new LoginRequest();
		user.setUsername("testUser1");
		user.setPassword("testPassword1");
		
		ResponseEntity<?> result = authController.authenticateUser(user);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
		logger.info("End Test login user 2");
	}
}
