package pl.tarnowski.individualprogrammingproject.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tarnowski.individualprogrammingproject.dao.UserRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;

import java.util.List;
import java.util.Optional;

@RestController
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(users));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        //  using for registration
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        // using for updating user data
        Optional<User> userFromDb = userRepository.findById(user.getId());

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        //
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isEmpty() || wrongPassword(userFromDb, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

//    very simple way to authentication the user
    private boolean wrongPassword(Optional<User> userFromDb, User user) {
        return !userFromDb.get().getPassword().equals(user.getPassword());
    }
}
