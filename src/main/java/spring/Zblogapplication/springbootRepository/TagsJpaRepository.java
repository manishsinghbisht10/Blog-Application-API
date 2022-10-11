package spring.Zblogapplication.springbootRepository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import spring.Zblogapplication.springbootEntity.Tag;

@Repository
public interface TagsJpaRepository extends JpaRepository<Tag, Integer>{
	
	Tag findByName(String tagName);
	
	@Query("select distinct t from Tag t join t.post p where p.isPublished is true")
	public List<Tag> getAllTagsUnique();
}
