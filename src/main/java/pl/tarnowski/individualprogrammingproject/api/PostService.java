package pl.tarnowski.individualprogrammingproject.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tarnowski.individualprogrammingproject.dao.PostRepository;
import pl.tarnowski.individualprogrammingproject.dao.UserRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Post;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;

import java.util.List;
import java.util.Optional;

@RestController
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/posts")
    public ResponseEntity addPost(@RequestHeader("username") String username, @RequestBody String postBody) {
        Optional<User> userFromDb = userRepository.findByUsername(username);

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = new Post(userFromDb.get(), postBody);
        Post savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/posts")
    public ResponseEntity getPosts() throws JsonProcessingException {

        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(posts));
    }
}
