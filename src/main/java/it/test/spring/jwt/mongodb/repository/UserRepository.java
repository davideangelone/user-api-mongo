package it.test.spring.jwt.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.test.spring.jwt.mongodb.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	  Optional<User> findByUsername(String username);
	  
	  Optional<User> findByEmail(String email);

	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);
	  
	  List<User> findByNationality(String nationality);
	  
	  @Query("{ 'age' : { $gt: ?0, $lt: ?1 } }")
	  List<User> findUsersByAgeBetween(int ageFrom, int ageTo);
	  
}
