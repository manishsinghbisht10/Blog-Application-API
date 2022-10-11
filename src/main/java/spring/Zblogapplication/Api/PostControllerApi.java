package spring.Zblogapplication.Api;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.Zblogapplication.service.PostService;
import spring.Zblogapplication.service.TagService;
import spring.Zblogapplication.springbootEntity.Post;
import spring.Zblogapplication.springbootEntity.Tag;
import spring.Zblogapplication.springbootEntity.User;
import spring.Zblogapplication.springbootRepository.UserJpaRepository;

@RestController
public class PostControllerApi {


	@Autowired
	 PostService postService;
	
	@Autowired
	 TagService tagService;
	
	@Autowired
	 UserJpaRepository userService;
	
	@GetMapping("/api/showDraft")
	public ResponseEntity<List<Post>> showDraft(){
		String uName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!uName.equals("anonymousUser")) {
			User userName=userService.findByUsername(uName);
			if(userName.getRoles().equals("ADMIN")) {
				List<Post>post=postService.getAllDraft();
				return new ResponseEntity<List<Post>>(post, HttpStatus.OK);
			}else {
				String name=userName.getName();
				List<Post>post=postService.findPostByName(name);
				return new ResponseEntity<List<Post>>(post, HttpStatus.OK);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/api/readPost/{id}")
	public ResponseEntity<Post> readPost(@PathVariable("id") int id) {
		Post post = postService.getPostById(id);
		System.out.println(post);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
		
		/*@GetMapping("/employees/{id}")
		EntityModel<Employee> one(@PathVariable Long id) {
			  Employee employee = repository.findById(id) 
		      .orElseThrow(() -> new EmployeeNotFoundException(id));

		  return assembler.toModel(employee);
		}*/
	}
	
	@DeleteMapping("/api/deletePost/{id}")
	public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") int id) {
		Post post=postService.getPostById(id);
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user=userService.findByUsername(name);
		if(post!=null&&((user.getRoles().equals("ADMIN"))||user.getName().equals(post.getName()))) {
			postService.deletePostById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("api/createPost")
	public ResponseEntity<HttpStatus> createPost(@RequestBody Post post,@RequestParam("tags")String tags){
		try {
			LocalDateTime datetime = LocalDateTime.now();  
			post.setCreatedAt(datetime);
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			User user=userService.findByUsername(name);
		    String strTags[]=tags.split(",");
			List<Tag> tagList=new ArrayList<>();
			for(int i=0;i<strTags.length;i++) {
				Tag tempTag=new Tag();
				tempTag=tagService.findTagByName(strTags[i]);
				if(tempTag==null) {
					Tag newTag=new Tag();
					newTag.setName(strTags[i]);
					tagService.saveTag(newTag);
					tagList.add(newTag);
				}else {
					tagList.add(tempTag);
				}
			} 
			String excerpt=post.getUserBlog();
			if(excerpt.length()>200)post.setExcerpt(excerpt.substring(0,200));
			else post.setExcerpt(excerpt);
			post.setTags(tagList);	
			post.setUserId(user);
			post.setName(user.getName());
			postService.savePost(post);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping("api/updatePost/{id}")
	public ResponseEntity<HttpStatus> updatePost(@RequestBody Post newPost,@PathVariable("id") int id,@RequestParam("tags")String tags){
		Post post=postService.getPostById(id);
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user=userService.findByUsername(name);
		if((user.getRoles().equals("ADMIN"))||user.getName().equals(post.getName())) {
		post.setUserBlog(newPost.getUserBlog());
		post.setTitle(newPost.getTitle());
		post.setIsPublished(newPost.getIsPublished());
		post.setPublishedAt(newPost.getPublishedAt());//place
		LocalDateTime datetime = LocalDateTime.now();  
		post.setUpdatedAt(datetime);
		String strTags[]=tags.split(",");
		List<Tag> tagList=new ArrayList<>();
		for(int i=0;i<strTags.length;i++) {
			Tag tempTag=new Tag();
			tempTag=tagService.findTagByName(strTags[i]);
			if(tempTag==null) {
				Tag newTag=new Tag();
				newTag.setName(strTags[i]);
				tagService.saveTag(newTag);
				tagList.add(newTag);
			}else {
				tagList.add(tempTag);
			}
		} 
		String excerpt=newPost.getUserBlog();
		if(excerpt.length()>200)post.setExcerpt(excerpt.substring(0,200));
		else post.setExcerpt(excerpt);
		post.setTags(tagList);
		postService.savePost(post);
		return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	} 
	@GetMapping("/api/home")
	public ResponseEntity<List<Post>> getData (@RequestParam(value="pageNumber",defaultValue = "1", required = false)int pageNumber,
			@RequestParam(value="sortBy",defaultValue = "0",required = false)int val,
			@RequestParam(value="search", defaultValue = "",required = false)String search,                                        
			@RequestParam(value="tag",defaultValue="",required=false)String[] tags,
			@RequestParam(value="author",defaultValue="",required=false)String[] author){
			List<Post>post=new ArrayList<Post>();
			if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//only filter
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAuthorAndTag(pageable, tags, author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(!search.equals(""))&&(val==0||val==1)) {//filter and search old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSearchOnAuthorAndTag(pageable, tags, search, author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==2)) {//filter sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSortAuthorAndTagASC(pageable, tags, author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter sort old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSortAuthorAndTagDESC(pageable, tags, author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(!search.equals(""))&&(val==2)) {//filter search sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSearchAndSortAuthorTag(pageable, tags, search, author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(!search.equals(""))&&(val==0||val==1)) {//filter pe search
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySearch(pageable, tags, search,author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(!search.equals(""))&&val==2) {//filter and search in sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySearchASC(pageable, tags, search,author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&val==2) {//filter and sort new
				for(int i=0;i<tags.length;i++)System.out.println(tags[i]);
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySortASC(pageable, tags,author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter and sort old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySortDESC(pageable, tags,author);
				post=tempPost.getContent();
			}
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter and sort
				for(int i=0;i<tags.length;i++)System.out.println(tags[i]); //only filter
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.findAllByTagsName(pageable, tags,author);
				post=tempPost.getContent();
			}
			else if(search.equals("")&&val==2) {//new sort
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.sortTimeASC(pageable);
				post=tempPost.getContent();
			}
			else if(search.equals("")&&val==1){  //old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.sortTimeDESC(pageable);
				post=tempPost.getContent();
			}
			else if((!search.equals(""))&&val==2) {//search and new sort
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.searchASC(pageable,search);
				post=tempPost.getContent();
			}
			else if((!search.equals(""))&&val==1) {//search with old sort
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.search(pageable,search);
				post=tempPost.getContent();
			}
			else if(!search.equals("")&&val==0) {//only searching
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.search(pageable,search);
				post=tempPost.getContent();
			}
			else {
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.getAllPost(pageable);
				post=tempPost.getContent();
			}
			return new ResponseEntity<List<Post>>(post, HttpStatus.OK);
		}
}	
