package pl.tarnowski.individualprogrammingproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;
import pl.tarnowski.individualprogrammingproject.dao.entity.UserDetails;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUser(User user);
}
