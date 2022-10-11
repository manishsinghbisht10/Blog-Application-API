package spring.Zblogapplication.springbootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import spring.Zblogapplication.springbootEntity.User;
import spring.Zblogapplication.springbootRepository.UserJpaRepository;

@Controller
public class UserController {

	@Autowired
	UserJpaRepository userService;
	
	@GetMapping("/saveSingInData")
	public String saveSignInData(@ModelAttribute User user) {
		user.setRoles("USER");
		userService.save(user);
		return "sucess";
	}
	
	@GetMapping("/signInPage")
	public String signInPage() {
		return "signIn";
	}
	
	@GetMapping("/login")
	public String viewLogin() {
		return "login";
	}
	@PostMapping("/login")
	public String loginView() {
		return "redirect:/home";
	}
		
	@GetMapping("/logout-sucess")
	public String logout() {
		return "login";
	}
	
	
}
