package pl.tarnowski.individualprogrammingproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
