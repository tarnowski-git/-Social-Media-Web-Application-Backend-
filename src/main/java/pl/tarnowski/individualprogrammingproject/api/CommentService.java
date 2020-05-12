package pl.tarnowski.individualprogrammingproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tarnowski.individualprogrammingproject.dao.CommentRepository;
import pl.tarnowski.individualprogrammingproject.dao.PostRepository;
import pl.tarnowski.individualprogrammingproject.dao.UserRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Comment;
import pl.tarnowski.individualprogrammingproject.dao.entity.Post;
import pl.tarnowski.individualprogrammingproject.dao.entity.User;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/comments/all")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        List<Comment> commentsFromDb = commentRepository.findAllByPostId(postId);
        if (commentsFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return new ResponseEntity<>(commentsFromDb, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "postId") Long postId, @Valid @RequestBody Comment comment) {
        Optional<Post> postFromDb = postRepository.findById(postId);
        if (postFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Optional<User> userFromDb = userRepository.findById(comment.getUser().getId());
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        comment.setUser(userFromDb.get());
        comment.setPost(postFromDb.get());
        comment.setCreatedAt(getCurrentTimestamp());

        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId, @Valid @RequestBody Comment commentRequest ) {
        if (!postRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Optional<Comment> commentFromDb = commentRepository.findById(commentId);
        if (commentFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Comment comment = commentFromDb.get();
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(getCurrentTimestamp());

        Comment updatedComment = commentRepository.save(comment);
        return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId) {
        Optional<Comment> commentFromDb = commentRepository.findByIdAndPostId(commentId, postId);
        if (commentFromDb.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        commentRepository.delete(commentFromDb.get());
        return ResponseEntity.ok().build();
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