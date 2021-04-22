package it.test.spring.jwt.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.test.spring.jwt.mongodb.models.ERole;
import it.test.spring.jwt.mongodb.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	
	  Optional<Role> findByName(ERole name);
	  
}
