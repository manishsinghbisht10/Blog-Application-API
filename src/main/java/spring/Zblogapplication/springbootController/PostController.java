package spring.Zblogapplication.springbootController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.Zblogapplication.service.TagService;
import spring.Zblogapplication.service.PostService;
import spring.Zblogapplication.springbootEntity.Tag;
import spring.Zblogapplication.springbootEntity.User;
import spring.Zblogapplication.springbootEntity.Post;
import spring.Zblogapplication.springbootRepository.UserJpaRepository;

@Controller
public class PostController {
	
	@Autowired
	 PostService postService;
	
	@Autowired
	 TagService tagService;
	
	@Autowired
	 UserJpaRepository userService;
	
	@GetMapping("/showAddForm")
	public String showAddForm(Model theModel) {
		Post post=new Post();
		theModel.addAttribute("object", post);
		String uName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!uName.equals("anonymousUser")) {
			User userName=userService.findByUsername(uName);
			theModel.addAttribute("role", userName.getRoles());
		}
		return "addPost";
	}
	
	@GetMapping("/showDraft")
	public String showDraft(Model theModel) {
		String uName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!uName.equals("anonymousUser")) {
			User userName=userService.findByUsername(uName);
			if(userName.getRoles().equals("ADMIN")) {
				theModel.addAttribute("post", postService.getAllDraft());
			}else {
				String name=userName.getName();
				List<Post>post=postService.findPostByName(name);
				if(post.size()==0)return "noDraft";
				theModel.addAttribute("post", post);
			}
			return "draft";
		}else{
			return "noDraft";
		}
	}

	
	@GetMapping("/saveDraft")
	public String saveDraft(@RequestParam("id")int id) {
		Post post=postService.getPostById(id);
		post.setIsPublished(true);
		postService.savePost(post);
		return "redirect:/home";
	}
	
	
	@PostMapping("/savePost")
	public String savePost(@ModelAttribute("object") Post post,@RequestParam("tag") String tagString) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user=userService.findByUsername(name);
		String str=user.getName();
		if(post.getId()!=0) {
			Post tempPost=postService.getPostById(post.getId());
			post.setCreatedAt(tempPost.getCreatedAt());
			LocalDateTime datetime = LocalDateTime.now();  
		    post.setUpdatedAt(datetime);
		}else {
			LocalDateTime datetime = LocalDateTime.now();  
		    post.setCreatedAt(datetime);
		}
		String tags[]=tagString.split(",");
		List<Tag> tagList=new ArrayList<>();
		for(int i=0;i<tags.length;i++) {
			Tag tempTag=new Tag();
			tempTag=tagService.findTagByName(tags[i]);
			if(tempTag==null) {
				Tag newTag=new Tag();
				newTag.setName(tags[i]);
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
		post.setName(str);
		post.setUserId(user);
		postService.savePost(post);
		return "redirect:/home";
	}
	
	@GetMapping("/")
	public String homePage() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String getData (@RequestParam(value="pageNumber",defaultValue = "1", required = false)int pageNumber,
			@RequestParam(value="sortBy",defaultValue = "0",required = false)int val,
			@RequestParam(value="search", defaultValue = "",required = false)String search,                                        
			@RequestParam(value="tag",defaultValue="",required=false)String[] tags,
			@RequestParam(value="author",defaultValue="",required=false)String[] author,
			Model theModel){
			String uName = SecurityContextHolder.getContext().getAuthentication().getName();
			if(!uName.equals("anonymousUser")) {
				User userName=userService.findByUsername(uName);
				theModel.addAttribute("role", userName.getRoles());
			}else theModel.addAttribute("role", "anonymousUser");
			
			if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//only filter
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAuthorAndTag(pageable, tags, author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 11);
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(!search.equals(""))&&(val==0||val==1)) {//filter and search old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSearchOnAuthorAndTag(pageable, tags, search, author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 12);
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==2)) {//filter sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSortAuthorAndTagASC(pageable, tags, author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 13);
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter sort old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSortAuthorAndTagDESC(pageable, tags, author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 14);
			}
			else if(((tags!=null&&tags.length>0)&&(author!=null&&author.length>0))&&(!search.equals(""))&&(val==2)) {//filter search sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterSearchAndSortAuthorTag(pageable, tags, search, author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 15);
			}
			// non And of tag and author
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(!search.equals(""))&&(val==0||val==1)) {//filter pe search
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySearch(pageable, tags, search,author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 7);
			}
			
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(!search.equals(""))&&val==2) {//filter and search in sort new
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySearchASC(pageable, tags, search,author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 8);
			}
			
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&val==2) {//filter and sort new
				for(int i=0;i<tags.length;i++)System.out.println(tags[i]);
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySortASC(pageable, tags,author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 9);
			}
			
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter and sort old
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.filterAllPostBySortDESC(pageable, tags,author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 10);
			}
			
			else if(((tags!=null&&tags.length>0)||(author!=null&&author.length>0))&&(search.equals(""))&&(val==0||val==1)) {//filter and sort
				for(int i=0;i<tags.length;i++)System.out.println(tags[i]); //only filter
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.findAllByTagsName(pageable, tags,author);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value", 6);
			}
			
			else if(search.equals("")&&val==2) {//new sort
					Pageable pageable = PageRequest.of(pageNumber-1, 4);
					Page<Post>tempPost = postService.sortTimeASC(pageable);
					List<Post>post=tempPost.getContent();
					theModel.addAttribute("post",post);
					theModel.addAttribute("currentPage", pageNumber);
					theModel.addAttribute("totalPages", tempPost.getTotalPages());
					theModel.addAttribute("value", 2);
			}
			else if(search.equals("")&&val==1){  //old
					Pageable pageable = PageRequest.of(pageNumber-1, 4);
					Page<Post>tempPost = postService.sortTimeDESC(pageable);
					List<Post>post=tempPost.getContent();
					theModel.addAttribute("post",post);
					theModel.addAttribute("currentPage", pageNumber);
					theModel.addAttribute("totalPages", tempPost.getTotalPages());
					theModel.addAttribute("value", 1);
			}
			else if((!search.equals(""))&&val==2) {//search and new sort
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.searchASC(pageable,search);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value",5);
			}
			//old
			else if((!search.equals(""))&&val==1) {//search with old sort
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.search(pageable,search);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value",4);
			}
			else if(!search.equals("")&&val==0) {//only searching
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.search(pageable,search);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("value",3);
			}
			else {
				Pageable pageable = PageRequest.of(pageNumber-1, 4);
				Page<Post>tempPost = postService.getAllPost(pageable);
				List<Post>post=tempPost.getContent();
				theModel.addAttribute("post",post);
				theModel.addAttribute("currentPage", pageNumber);
				theModel.addAttribute("totalPages", tempPost.getTotalPages());
				theModel.addAttribute("totalItems",tempPost.getTotalElements());
				theModel.addAttribute("value", 0);
			}
			theModel.addAttribute("searchVal",search);
			theModel.addAttribute("tag", tags); 
			theModel.addAttribute("sortBy", val); 
			theModel.addAttribute("author", author); 
			theModel.addAttribute("tags",tagService.getAllTagsUnique());
			theModel.addAttribute("allAuthor", postService.getAuthor());
			return "home";
		}
	
	@PostMapping("/readPost")
	public String readPost(Model theModel,int id) {
		theModel.addAttribute("BlogPost",postService.getPostById(id));
		String name = SecurityContextHolder.getContext().getAuthentication().getName();	
		if(name=="anonymousUser") {
			theModel.addAttribute("userName", name);
			theModel.addAttribute("admin", "");
			theModel.addAttribute("user", "");
			
		}else {
			User user=userService.findByUsername(name);
			String userName=user.getName();
			String admin=user.getRoles();
			theModel.addAttribute("userName", userName);
			theModel.addAttribute("user", user);
			theModel.addAttribute("admin", admin);
		}
		System.out.println(postService.getPostById(id).getName());
		return "readPost";
	}
	
	@GetMapping("/updatePost{id}")
	public String updatePost(@RequestParam("id") int theId,Model theModel) {
		Post post=postService.getPostById(theId);
		List<Tag> tag=post.getTags();
		String inputTags="";
		for(int i=0;i<tag.size();i++)inputTags=inputTags+tag.get(i).getName()+",";
		theModel.addAttribute("object",post);
		theModel.addAttribute("tag",inputTags);
		return "addPost";
	}
	
	@GetMapping("/deletePost")
	public String deletePost(@RequestParam("id")int theId,Model theModel) {
		postService.deletePostById(theId);
		return "redirect:/home";
	}
}

