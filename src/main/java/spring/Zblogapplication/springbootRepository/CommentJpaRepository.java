package spring.Zblogapplication.springbootRepository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.Zblogapplication.springbootEntity.Comments;


@Repository
public interface CommentJpaRepository extends JpaRepository<Comments, Integer>{

}
