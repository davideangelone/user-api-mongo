package it.test.spring.jwt.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.test.spring.jwt.mongodb.models.User;
import it.test.spring.jwt.mongodb.models.dto.UserDto;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	  Optional<User> findByUsername(String username);
	  
	  Optional<UserDto> findByEmail(String email);

	  boolean existsByUsername(String username);

	  boolean existsByEmail(String email);
	  
	  List<UserDto> findByNationality(String nationality);
	  
	  @Query("{ 'age' : { $gte: ?0, $lte: ?1 } }")
	  List<UserDto> findByAgeBetween(int ageFrom, int ageTo);

	  void deleteByUsername(String username);
	  
}
