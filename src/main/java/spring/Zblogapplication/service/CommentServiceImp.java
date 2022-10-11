package spring.Zblogapplication.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import spring.Zblogapplication.springbootEntity.Comments;
import spring.Zblogapplication.springbootRepository.CommentJpaRepository;

@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	CommentJpaRepository service;
	
	@Override
	public void saveComment(Comments com) {
		service.save(com);
	}

	@Override
	public List<Comments> getAllComments() {
		return service.findAll();
	}



	@Override
	public void deleteById(int Id) {
		service.deleteById(Id);
	}

	@Override
	public Comments getCommentById(int id) {
		return service.findById(id).get();
	}
}
