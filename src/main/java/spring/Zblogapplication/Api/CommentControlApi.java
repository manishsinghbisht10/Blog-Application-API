package spring.Zblogapplication.Api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.Zblogapplication.service.CommentService;
import spring.Zblogapplication.service.PostService;
import spring.Zblogapplication.springbootEntity.Comments;
import spring.Zblogapplication.springbootEntity.Post;
@RestController
public class CommentControlApi {
	
	@Autowired
	CommentService commentService;
	@Autowired
	PostService postService;
	
	@GetMapping("/api/readComment/{id}")
	public ResponseEntity<Comments> readComment(@PathVariable("id") int id) {
		try {
			Comments comment=commentService.getCommentById(id);
			return new ResponseEntity<>(comment,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/api/createComment/{id}")
	public ResponseEntity<HttpStatus> createComment(@RequestBody Comments comment,@PathVariable("id")int id){
		try {
			LocalDateTime dateTime = LocalDateTime.now();  
			comment.setCreatedAt(dateTime);
			Post post=postService.getPostById(id);
			comment.setPostId(post);
			commentService.saveComment(comment);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/api/deleteComment/{id}")
	public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") int id) {
		try {
			commentService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/api/updateComment/{id}")
	public ResponseEntity<HttpStatus> updateComment(@PathVariable("id") int id,@RequestBody Comments newComment) {
		try {
			Comments comment=commentService.getCommentById(id);
			comment.setName(newComment.getName());
			comment.setEmail(newComment.getEmail());
			comment.setComment(newComment.getComment());
			LocalDateTime dateTime = LocalDateTime.now();  
			comment.setUpdatedAt(dateTime);
			commentService.saveComment(comment);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
