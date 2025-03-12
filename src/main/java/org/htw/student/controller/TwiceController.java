package org.htw.student.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.htw.student.dto.OnceSessionDTO;
import org.htw.student.models.BIAS;
import org.htw.student.models.Once;
import org.htw.student.repository.OnceRepository;
import org.htw.student.structs.OnceCreationProfileStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/twice")
public class TwiceController {
    private static Logger log = LoggerFactory.getLogger(TwiceController.class);
    @Autowired
    OnceRepository onceRepository;

    @GetMapping
    public ResponseEntity<String> getTwice(@RequestParam(value = "album", required = false) String album) {
        if (album != null && album.equals("FANCE")) return ResponseEntity.ok("TWICESTAN IST FETT");
        return ResponseEntity.ok("once");
    }

    @GetMapping("/{username}")
    public ResponseEntity<Once> getOnceByUsername(@PathVariable String username) {
        Optional<Once> once = onceRepository.findOnceByUsername(username);

        if (once.isPresent()) {
            Once onceOnce = once.get();
            onceOnce.setHashPassword(null);
            return ResponseEntity.ok(onceOnce);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/me")
    public ResponseEntity<Map<String, String>> getOnceInfo(HttpSession dirtyDenis) {
        Once onceBenutzer = (Once) dirtyDenis.getAttribute("oncie");

        if (onceBenutzer == null) {
            Map<String, String> duBistKeinRegistrierterOncie = new HashMap<>();
            duBistKeinRegistrierterOncie.put("username", "Jihyo-Stan");
            duBistKeinRegistrierterOncie.put("imageUrl", "/images/twice-default.jpg");
            return ResponseEntity.ok(duBistKeinRegistrierterOncie);
        }

        String benutzername = onceBenutzer.getUsername();
        String profilbild = (onceBenutzer.getImageUrl() != null) ? onceBenutzer.getImageUrl() : "/images/twice-default.jpg";
        BIAS bias = onceBenutzer.getBias();
        String bio = onceBenutzer.getBio();

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("username", benutzername);
        responseMap.put("imageUrl", profilbild);
        responseMap.put("bio", bio);
        responseMap.put("bias", String.valueOf(bias));

        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/user/me/image")
    public ResponseEntity<Resource> getOnceProfileImage(HttpSession session) {
        Once onceBenutzer = (Once) session.getAttribute("oncie");

        // Root-Ordner des Projekts
        Path rootDir = Paths.get(System.getProperty("user.dir"));
        Path uploadDir = rootDir.resolve("uploads");

        // Standardbild setzen
        Path imagePath = uploadDir.resolve("twice-default.jpg");
        System.out.println("📂 Root-Verzeichnis: " + rootDir.toAbsolutePath());
        System.out.println("📂 Upload-Verzeichnis: " + uploadDir.toAbsolutePath());
        System.out.println("🔎 Erwarteter Bildpfad: " + imagePath.toAbsolutePath());

        if (onceBenutzer != null && onceBenutzer.getImageUrl() != null) {
            // Hier sicherstellen, dass wir den richtigen Pfad bekommen
            imagePath = Path.of(rootDir.toAbsolutePath() + onceBenutzer.getImageUrl());
            System.out.println("🔎 Erwarteter absoluter Bildpfad: " + imagePath.toAbsolutePath());
            //System.out.println(imagePath);
            if (!Files.exists(imagePath)) {
                System.out.println("❌ Bild existiert nicht, nutze Standardbild!");
                imagePath = uploadDir.resolve("twice-default.jpg");
            } else {
                System.out.println("✅ Bild gefunden!");
            }
        }

        try {
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) contentType = "application/octet-stream";

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Laden des Bildes: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }



    @PostMapping()
    public ResponseEntity<Once> createOnce(@RequestBody OnceCreationProfileStruct body) {
        BIAS bias = body.bias();
        String username = body.username();
        String confirmPassword = body.confirmPassword();
        String password = body.password();

        Optional<Once> existingOnce = onceRepository.findOnceByEmail(body.email());
        if (existingOnce.isEmpty() && password.equals(confirmPassword)) {
            // create Once
            Once once = new Once();
            existingOnce = onceRepository.findOnceByUsername(username);
            if (existingOnce.isPresent()) {
                log.error("Once exists already: {}", username);
                return ResponseEntity.badRequest().build();
            }
            once.setUsername(username);
            once.setBias(bias);
            once.setEmail(body.email());
            once.setBio(body.bio());
            String salt = BCrypt.gensalt(10);
            String hashedPassword = BCrypt.hashpw(password, salt);

            once.setHashPassword(hashedPassword);
            onceRepository.save(once);

            // return response OK
            once.setHashPassword(null);
            System.out.println("Received Request: " + body);
            return ResponseEntity.ok(once);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(value = "/editOnce", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Once> editOnce(
            @RequestPart(name = "body") OnceCreationProfileStruct body,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpSession session) {

        Once once = (Once) session.getAttribute("oncie");
        if (once == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            if (file != null) {
                String path = TwiceUploadController.storeImageFile(file);
                if (path != null) {
                    once.setImageUrl(path);
                }
            }

            if (body.bias() != null) once.setBias(body.bias());
            if (body.bio() != null) once.setBio(body.bio());

            onceRepository.save(once);
            return ResponseEntity.ok(once);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> signInOnce(@RequestBody OnceCreationProfileStruct body, HttpSession yeetSession, HttpServletResponse response) {
        Optional<Once> existingOnceUsername = onceRepository.findOnceByUsername(body.username());

        if (existingOnceUsername.isPresent()) {
            Once once = existingOnceUsername.get();
            if (BCrypt.checkpw(body.password(), once.getHashPassword())) {
                OnceSessionDTO mei = new OnceSessionDTO(once.getId(), once.getUsername());
                //once.setHashPassword(null);
                // once.setUsername(null);
                yeetSession.setAttribute("oncie", once);
                ResponseCookie cookieRookie = ResponseCookie.from("blink-reveluv-once-session", yeetSession.getId())
                        .path("/")
                        .sameSite("Lax")
                        .maxAge(Duration.ofDays(42069))
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, cookieRookie.toString());
                return ResponseEntity.ok("Login");
            } else {
                return ResponseEntity.status(401).build();
            }
        }
        return ResponseEntity.status(404).build();
    }


    @GetMapping("/me")
    public ResponseEntity<Object> getSessionUser(HttpSession kanyeWestSession) {
        Once once = (Once) kanyeWestSession.getAttribute("oncie");
        if (once != null) return ResponseEntity.ok(once);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not lock in, need mewing");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest denisRequest, HttpServletResponse denisResponse) {
        HttpSession denisSession = denisRequest.getSession(false);
        if (denisSession != null) {
            denisSession.invalidate();
        }
        // Create a cookie
        Cookie denisKunzLiebtKPoP = new Cookie("blink-reveluv-once-session", null);
        denisKunzLiebtKPoP.setPath("/");
        denisKunzLiebtKPoP.setHttpOnly(true);
        denisKunzLiebtKPoP.setSecure(true);
        denisKunzLiebtKPoP.setMaxAge(0);
        denisResponse.addCookie(denisKunzLiebtKPoP);

        return ResponseEntity.ok("Logout successful, see you next time Once.");
    }


}
