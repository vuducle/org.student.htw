package org.htw.student.controller;

import jakarta.servlet.http.HttpSession;
import org.htw.student.models.Once;
import org.htw.student.models.TwicePost;
import org.htw.student.repository.OnceRepository;
import org.htw.student.repository.TwicePostRepository;
import org.htw.student.structs.TwicePostCreationStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/twice-post")
public class TwicePostController {
    @Autowired
    TwicePostRepository twicePostRepository;
    OnceRepository onceRepository;
    TwiceUploadController twiceUploadController;

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

    @GetMapping("/{onceId}")
    public ResponseEntity<List<TwicePost>> getOncePost(@PathVariable String onceId) {
        return ResponseEntity.ok(twicePostRepository.findTwicePostById(onceId));
    }

    @DeleteMapping("/{onceId}")
    public ResponseEntity<?> deletePost(@PathVariable String onceId, HttpSession session) {
        String username = (String) session.getAttribute("oncie");

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not sign up");
        }

        Optional<TwicePost> postOptional = twicePostRepository.findById(onceId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TwicePost post = postOptional.get();
        if (!post.getOnce().getUsername().equals(username)) {
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
}
