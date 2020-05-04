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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public ResponseEntity<Post> addPost(@RequestHeader("username") String username, @RequestBody String postBody) {
        Optional<User> userFromDb = userRepository.findByUsername(username);
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = new Post(userFromDb.get(), postBody, getCurrentTimestamp(), getCurrentTimestamp());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/posts/all")
    public ResponseEntity<List<Post>> getPosts() throws JsonProcessingException {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/posts")
    public ResponseEntity<Post> updatePost(@RequestParam Long id, @RequestBody String postBody) {
        Optional<Post> postFromDB = postRepository.findById(id);
        if (postFromDB.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Post updatedPost = postFromDB.get();
        updatedPost.setBody(postBody);
        updatedPost.setUpdatedAt(getCurrentTimestamp());
        postRepository.save(updatedPost);
        return new ResponseEntity<Post>(updatedPost, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/posts")
    public ResponseEntity<Void> deletePost(@RequestParam Long id) {
        Optional<Post> postFromDB = postRepository.findById(id);
        if (postFromDB.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        postRepository.delete(postFromDB.get());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     *  Private method returning a Timestamp object for MySQL database.
     */
    private Timestamp getCurrentTimestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Warsaw");
        Calendar calendar = Calendar.getInstance(timeZone, Locale.getDefault());
        return new Timestamp(calendar.getTimeInMillis());
    }
}
