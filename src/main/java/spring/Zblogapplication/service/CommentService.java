package spring.Zblogapplication.service;

import java.util.List;

import spring.Zblogapplication.springbootEntity.Comments;

public interface CommentService {
	public List<Comments> getAllComments();

    public Comments getCommentById(int id);
    
    public void deleteById(int Id);
    
	public void saveComment(Comments com);
 
}
