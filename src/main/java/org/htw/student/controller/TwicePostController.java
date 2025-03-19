package org.htw.student.controller;

import jakarta.servlet.http.HttpSession;
import org.htw.student.models.Once;
import org.htw.student.models.TwiceComment;
import org.htw.student.models.TwicePost;
import org.htw.student.repository.OnceRepository;
import org.htw.student.repository.TwiceCommentRepository;
import org.htw.student.repository.TwicePostRepository;
import org.htw.student.structs.TwicePostCreationStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/twice-post")
public class TwicePostController {
    private static final Logger log = LoggerFactory.getLogger(TwicePostController.class);
    @Autowired
    TwicePostRepository twicePostRepository;

    OnceRepository onceRepository;
    TwiceUploadController twiceUploadController;
    @Autowired
    private TwiceCommentRepository twiceCommentRepository;

    @PostMapping
    public ResponseEntity<?> createTwicePost(@RequestPart("body") TwicePostCreationStruct body,
                                             @RequestPart(value = "twiceFile", required = false) MultipartFile[] twiceFile,
                                             HttpSession authentificated) {
        // Wenn Once eingeloggt ist
        Once username = (Once) authentificated.getAttribute("oncie");
        //Optional<Once> once =  onceRepository.findOnceByAuthentificated(username);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nicht eingeloggt, Denis");
        }
        //Once getOnce = once.get();

        try {
            TwicePost twicePost = new TwicePost();
            List<String> asianGirls = new ArrayList<>();
            // Title
            String triesnhaAmeilyaTitle = body.title();
            // Content
            String denisKunzLiebtLangeZeilenContent = body.content();
            // Member name like Dahyun, Jihyo or Tzuyu
            String minaSanaMomoMemberName = body.memberName();

            if (twiceFile != null) {
                // TODO: I think it should work?
                for(MultipartFile juliaNguyen : twiceFile) {
                    String path = TwiceUploadController.storeImageFile(juliaNguyen);
                    if (path != null) {
                        asianGirls.add(path);
                        if (asianGirls.size() > 5) return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Zu viele Dateien");
                    }
                }
            }

            twicePost.setOnce(username);
            twicePost.setTitle(triesnhaAmeilyaTitle);
            twicePost.setContent(denisKunzLiebtLangeZeilenContent);
            twicePost.setMemberName(minaSanaMomoMemberName);
            twicePost.setImageUrl(asianGirls);
            twicePostRepository.save(twicePost);
            return ResponseEntity.ok(twicePost);

        } catch (Exception juliaNguyenException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<TwicePost>> getAllTwicePost() {
        return ResponseEntity.ok(twicePostRepository.findAll());
    }

    @GetMapping("/post/{onceId}")
    public ResponseEntity<?> getTwicePost(@PathVariable String onceId) {
        Optional<TwicePost> post = twicePostRepository.findById(onceId);

        if (post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(post.get());
    }

    @DeleteMapping("/post/{onceId}")
    public ResponseEntity<?> deletePost(@PathVariable String onceId, HttpSession session) {
        Once username = (Once) session.getAttribute("oncie");

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not sign up");
        }

        Optional<TwicePost> postOptional = twicePostRepository.findById(onceId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TwicePost post = postOptional.get();
        if (!post.getOnce().getUsername().equals(username.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }

        if (post.getImageUrl() != null) {
            for (String imageUrl : post.getImageUrl()) {
                TwiceUploadController.deleteImage(imageUrl);
            }
        }

        twicePostRepository.deleteById(onceId);
        return ResponseEntity.ok("Deleted post");
    }

    @PostMapping("/post/{onceId}/upvote")
    public ResponseEntity<Object> upvoteTwicePost(@PathVariable String onceId, HttpSession session) {
        Once once = (Once) session.getAttribute("oncie");

        Optional<TwicePost> twicePostOptional = twicePostRepository.findById(onceId);
        if (twicePostOptional.isEmpty()) return ResponseEntity.notFound().build();

        TwicePost post = twicePostOptional.get();
        if (!post.getUpvotedBy().contains(once.getId())) {
            post.getUpvotedBy().add(once.getId());
            twicePostRepository.save(post);
        }

        return ResponseEntity.ok("Upvoted post");
    }

    @PostMapping("/post/{onceId}/comment")
    public ResponseEntity<Object> addTwiceComment(@PathVariable String onceId, @RequestBody Map<String, String> body, HttpSession session) {
        Once once = (Once) session.getAttribute("oncie");
        if (once == null) {
            log.error("User not signed in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not signed in");
        }
        log.info("User logging in: {}", once);
        Optional<TwicePost> postOptional = twicePostRepository.findById(onceId);
        if (postOptional.isEmpty()) return ResponseEntity.notFound().build();

        TwicePost twicePost = postOptional.get();
        TwiceComment twiceComment = new TwiceComment();
        twiceComment.setPost(twicePost);
        twiceComment.setOnce(once);
        twiceComment.setContent(body.get("content"));
        twiceCommentRepository.save(twiceComment);
        return ResponseEntity.ok("Comment added");
    }

    @PostMapping("/comment/{commentId}/upvote")
    public ResponseEntity<Object> upvoteComment(@PathVariable String commentId, HttpSession session) {
        Once once = (Once) session.getAttribute("oncie");
        if (once == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not signed in");

        Optional<TwiceComment> commentOptional = twiceCommentRepository.findById(commentId);
        if (commentOptional.isEmpty()) return ResponseEntity.notFound().build();

        TwiceComment comment = commentOptional.get();
        if (!comment.getUpvotedBy().contains(once.getId())) {
            comment.getUpvotedBy().add(once.getId());
            twiceCommentRepository.save(comment);
        }

        return ResponseEntity.ok("Upvoted comment");
    }
}