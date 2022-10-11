package spring.Zblogapplication.springbootRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.Zblogapplication.springbootEntity.User;
@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
	 
}
