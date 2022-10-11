package spring.Zblogapplication.springbootRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.Zblogapplication.springbootEntity.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Integer>{
	@Query("select p from Post p where p.isPublished is false order by p.id asc")
	public List<Post> getAllDraft();
	
	@Query("select p from Post p where p.name like %:keyword% and p.isPublished is false order by p.id asc" )
	public List<Post> findPostByName(@Param("keyword")String keyword);
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name like %:keyword%) or (p.name like %:keyword%) or (p.userBlog like %:keyword%)) order by p.id asc") 
	public Page<Post> search(Pageable pagable,@Param("keyword") String keyword);
	
	//new
	@Query("select distinct p from Post p join p.tags t where  p.isPublished is true and ((t.name like %:keyword%) or (p.name like %:keyword%) or (p.userBlog like %:keyword%)) order by p.id desc") 
	public Page<Post> searchASC(Pageable pagable,@Param("keyword") String keyword);
	
	@Query("select p from Post p where p.isPublished is true order by p.id asc" )
	public Page<Post> sortTimeDESC(Pageable pageable);
	
	@Query("select p from Post p where p.isPublished is true order by p.id desc")
	public Page<Post> sortTimeASC(Pageable pageable);//new
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name IN :tag) or (p.name IN :author))")//only filter
	public Page<Post> findAllByTagsName(Pageable pageable,@Param("tag") String[] tag,@Param("author")String[] author);
	
	//OR
	//filter search and sort old OR
	@Query("select distinct p from Post p join p.tags t  where p.isPublished is true and ((t.name like %:search%) or (p.name like %:search%) or (p.userBlog like %:search%) and ((t.name IN :tag) or (p.name IN :author)))")
	public Page<Post> filterAllPostBySearch(Pageable pageable,@Param("tag")String[] tag,@Param("search")String search,@Param("author")String[] author);
	
	//filter search and sort new OR
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name like %:search%) or (p.name like %:search%) or (p.userBlog like %:search%) and ((t.name IN :tag) or (p.name IN :author))) order by p.id desc")
	public Page<Post> filterAllPostBySearchASC(Pageable pageable,@Param("tag")String[] tag,@Param("search")String search,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and (((t.name IN :tag) or (p.name IN :author)) or (p.name IN :tag))")//filter and sort old
	public Page<Post> filterAllPostBySortDESC(Pageable pageable,@Param("tag")String[] tag,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t  where p.isPublished is true and (((t.name IN :tag) or (p.name IN :author)) or (p.name IN :tag)) order by p.id desc")//filter and sort new
	public Page<Post> filterAllPostBySortASC(Pageable pageable,@Param("tag")String[] tag,@Param("author")String[] author);
	
	@Query("select p from Post p where p.isPublished is true order by p.createdAt asc")
	public Page<Post> getAllPost(Pageable pageable);
	
	@Query("select distinct p from Post p where p.isPublished is true")
	public List<Post> getAuthor();
	
	//AND
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name IN :tag) and (p.name IN :author))")
	public Page<Post> filterAuthorAndTag(Pageable pageable,@Param("tag")String[] tag,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t  where p.isPublished is true and ((t.name like %:search%) or (p.name like %:search%) or (p.userBlog like %:search%) and ((t.name IN :tag) and (p.name IN :author)))")
	public Page<Post> filterSearchOnAuthorAndTag(Pageable pageable,@Param("tag")String[] tag,@Param("search")String search,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name IN :tag) and (p.name IN :author)) order by p.id desc")
	public Page<Post> filterSortAuthorAndTagASC(Pageable pageable,@Param("tag")String[] tag,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name IN :tag) and (p.name IN :author))")
	public Page<Post> filterSortAuthorAndTagDESC(Pageable pageable,@Param("tag")String[] tag,@Param("author")String[] author);
	
	@Query("select distinct p from Post p join p.tags t where p.isPublished is true and ((t.name like %:search%) or (p.name like %:search%) or (p.userBlog like %:search%) and ((t.name IN :tag) and (p.name IN :author))) order by p.id desc")
	public Page<Post> filterSearchAndSortAuthorTag(Pageable pageable,@Param("tag")String[] tag,@Param("search")String search,@Param("author")String[] author);
}
