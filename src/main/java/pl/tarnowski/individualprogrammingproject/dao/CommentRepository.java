package pl.tarnowski.individualprogrammingproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
