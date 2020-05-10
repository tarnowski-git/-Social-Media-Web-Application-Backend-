package pl.tarnowski.individualprogrammingproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tarnowski.individualprogrammingproject.dao.CommentRepository;
import pl.tarnowski.individualprogrammingproject.dao.entity.Comment;

import java.util.List;

@RestController
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("comments/all")
    public ResponseEntity<List<Comment>> getAllCommnets() {
        List<Comment> comments = commentRepository.findAll();

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
