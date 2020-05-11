package pl.tarnowski.individualprogrammingproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
