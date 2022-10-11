package spring.Zblogapplication.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.Zblogapplication.springbootEntity.Post;
import spring.Zblogapplication.springbootRepository.PostJpaRepository;

@Service
public class PostServiceImp implements PostService {

	@Autowired
	PostJpaRepository service;

	@Override
	public Post getPostById(int id) {
		return service.findById(id).get();
	}

	@Override
	public void deletePostById(int Id) {
		service.deleteById(Id);
	}

	@Override
	public void savePost(Post blog) {
		service.save(blog);
	}

	@Override
	public List<Post> getAllPost() {
		return service.findAll();
	}

	@Override
	public Page<Post> search(Pageable pagable, String keyword) {
		return service.search(pagable, keyword);
	}

	@Override
	public Page<Post> searchASC(Pageable pagable, String keyword) {
		return service.searchASC(pagable, keyword);
	}

	@Override
	public Page<Post> sortTimeDESC(Pageable pageable) {
		return service.sortTimeDESC(pageable);
	}

	@Override
	public Page<Post> sortTimeASC(Pageable pageable) {
		return service.sortTimeASC(pageable);
	}

	@Override
	public Page<Post> findAllByTagsName(Pageable pageable,String[] tag,String[] author) {
		return service.findAllByTagsName(pageable,tag,author);
	}

	@Override
	public Page<Post> filterAllPostBySearch(Pageable pageable, String[] tag, String search,String[]author) {
		return service.filterAllPostBySearch(pageable, tag, search, author);
	}

	@Override
	public Page<Post> filterAllPostBySearchASC(Pageable pageable, String[] tag, String search,String[]author) {
		return service.filterAllPostBySearchASC(pageable, tag, search,author);
	}

	@Override
	public Page<Post> filterAllPostBySortDESC(Pageable pageable, String[] tag,String[]author) {
		// TODO Auto-generated method stub
		return service.filterAllPostBySortDESC(pageable, tag,author);
	}

	@Override
	public Page<Post> filterAllPostBySortASC(Pageable pageable, String[] tag,String[]author) {
		// TODO Auto-generated method stub
		 return service.filterAllPostBySortASC(pageable, tag,author);
	}

	@Override
	public List<Post> findPostByName(String name) {
		return service.findPostByName(name);
	}

	@Override
	public Page<Post> getAllPost(Pageable pageable) {
		return service.getAllPost(pageable);
	}

	@Override
	public Set<String> getAuthor() {
		List<Post>post= service.getAuthor();
		Set<String>nameSet=new HashSet<>();
		for(int i=0;i<post.size();i++)nameSet.add(post.get(i).getName());
		return nameSet;
	}

	@Override
	public List<Post> getAllDraft() {
		return service.getAllDraft();
	}

	@Override
	public Page<Post> filterAuthorAndTag(Pageable pageable, String[] tag, String[] author) {
		return service.filterAuthorAndTag(pageable, tag, author);
	}

	@Override
	public Page<Post> filterSearchOnAuthorAndTag(Pageable pageable, String[] tag, String search, String[] author) {
		return service.filterSearchOnAuthorAndTag(pageable, tag, search, author);
	}

	@Override
	public Page<Post> filterSortAuthorAndTagASC(Pageable pageable, String[] tag, String[] author) {
		return service.filterSortAuthorAndTagASC(pageable, tag, author);
	}

	@Override
	public Page<Post> filterSortAuthorAndTagDESC(Pageable pageable, String[] tag, String[] author) {
		return service.filterSortAuthorAndTagDESC(pageable, tag, author);
	}

	@Override
	public Page<Post> filterSearchAndSortAuthorTag(Pageable pageable, String[] tag, String search, String[] author) {
		return service.filterSearchAndSortAuthorTag(pageable, tag, search, author);
	}	
}
