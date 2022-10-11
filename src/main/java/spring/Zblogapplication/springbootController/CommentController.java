package spring.Zblogapplication.springbootController;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.Zblogapplication.service.CommentService;
import spring.Zblogapplication.service.PostService;
import spring.Zblogapplication.springbootEntity.Comments;
import spring.Zblogapplication.springbootEntity.Post;

@Controller
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@Autowired
	PostService postService;
	
	@PostMapping("/saveComment/{id}")
	public String saveComment(@ModelAttribute Comments comment ,@PathVariable("id") int theId,Model theModel) {
		LocalDateTime datetime = LocalDateTime.now();  
	    comment.setCreatedAt(datetime);
		Post post=new Post();
		post=postService.getPostById(theId);
		comment.setPostId(post);
		commentService.saveComment(comment);
		theModel.addAttribute("BlogPost",postService.getPostById(theId));
		return "redirect:/home";
	}
	
	@PostMapping("/updateComment/{id}")
	public String updateComment(@ModelAttribute Comments comment,@PathVariable("id") int theId,Model theModel) {
		Comments com=commentService.getCommentById(theId);
		com.setName(comment.getName());
		com.setEmail(comment.getEmail());
		com.setComment(comment.getComment());
		commentService.saveComment(com);
		return "redirect:/home";
	}
	
	@GetMapping("/update")//comment
	public String update(Model theModel,int id) {
		theModel.addAttribute("obj", commentService.getCommentById(id));
		return "updateComment";
	}
	
	@GetMapping("/deleteComment")
	public String deleteComment(@RequestParam("id")int theId,Model theModel) {
		commentService.deleteById(theId);
		return "redirect:/home";
	}
}
