package pl.tarnowski.individualprogrammingproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
