package spring.Zblogapplication.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import spring.Zblogapplication.springbootEntity.User;
import spring.Zblogapplication.springbootRepository.UserJpaRepository;
@Service
public class MyUserDetailService implements UserDetailsService{

	@Autowired
	UserJpaRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=repo.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("Error");
		}
		return new MyUserDetailImp(user);
		
		
	}
//	public User user(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		return repo.save(user);
//	}

}
