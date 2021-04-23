package it.test.spring.jwt.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.test.spring.jwt.mongodb.models.ERole;
import it.test.spring.jwt.mongodb.models.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
	
	  Optional<Role> findByName(ERole name);
	  
}
