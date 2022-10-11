package spring.Zblogapplication.service;

import java.util.List;

import spring.Zblogapplication.springbootEntity.Tag;


public interface TagService {

	public List<Tag> getAllTags();
	
	public List<Tag> getAllTagsUnique();
      
	public void saveTag(Tag com);
	
	Tag findTagByName(String tagName);
 
}
