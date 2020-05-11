package pl.tarnowski.individualprogrammingproject.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class UserService {

    final String defaultImage = "https://res.cloudinary.com/dp9cngk9m/image/upload/v1588774216/noimg_tol9jq.jpg";

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        //  using for registration
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        user.setImageUrl(defaultImage);
        User savedUser = userRepository.save(user);
        UserDetails userDetails = new UserDetails(savedUser, "", 0, "", "", "");
        userDetailsRepository.save(userDetails);

        return ResponseEntity.ok(savedUser);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        // using for updating first and last names
        Optional<User> userFromDb = userRepository.findByUsername(username);

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        if (user.getFirst().equals("") || user.getLast().equals("")) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        User newUser = userFromDb.get();
        newUser.setFirst(user.getFirst());
        newUser.setLast(user.getLast());

        if (!(newUser.getImageUrl().equals(user.getImageUrl()))) {
            newUser.setImageUrl(user.getImageUrl());
        }
        if (newUser.getImageUrl().equals("")) {
            newUser.setImageUrl(defaultImage);
        }

        User updatedUser = userRepository.save(newUser);
        return ResponseEntity.ok(updatedUser);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isEmpty() || wrongPassword(userFromDb, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userFromDb.get());
    }

//    very simple way to authentication the user
    private boolean wrongPassword(Optional<User> userFromDb, User user) {
        return !userFromDb.get().getPassword().equals(user.getPassword());
    }
}
