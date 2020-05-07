package pl.tarnowski.individualprogrammingproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tarnowski.individualprogrammingproject.dao.UserDetailsRepository;
import pl.tarnowski.individualprogrammingproject.dao.UserRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;
import pl.tarnowski.individualprogrammingproject.dao.entity.UserDetails;

import java.util.List;
import java.util.Optional;

@RestController
public class UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/details/all")
    public ResponseEntity<List<UserDetails>> getAllUserDetails() {
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();

        return  ResponseEntity.ok(userDetailsList);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/details/{username}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable String username) {
        Optional<User> userFromDb = userRepository.findByUsername(username);
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<UserDetails> userDetailsFromDb = userDetailsRepository.findByUser(userFromDb.get());
        if (userDetailsFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return  ResponseEntity.ok(userDetailsFromDb.get());
    }


}
