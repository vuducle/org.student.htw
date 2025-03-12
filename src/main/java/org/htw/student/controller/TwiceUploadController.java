package org.htw.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class TwiceUploadController {
    private static final Logger log = LoggerFactory.getLogger(TwiceUploadController.class);
    private static final String UPLOAD_DIR = "uploads/";

    public ResponseEntity<String> uploadImage(MultipartFile file) {
        // We love to write verbose code
        if (file.isEmpty()) return ResponseEntity.badRequest().body("No file selected");
        try {
            // Create folder if not exists
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            // Filename
            String blackPink = UUID.randomUUID() + "_" + file.getOriginalFilename();
            // Pathname
            Path redVelvet = Paths.get(UPLOAD_DIR, blackPink);
            Files.write(redVelvet, file.getBytes());

            String twice = "/uploads/"+blackPink;
            return ResponseEntity.ok(twice);
        } catch (IOException itzyMidzy) {
            return ResponseEntity.status(500).body("Failed to upload image, internal server error");
        }
    }

    public static String storeImageFile(MultipartFile file)
    {
        if (file.isEmpty()) return null;
        try {
            // Create folder if not exists
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            // Filename
            String blackPink = UUID.randomUUID() + "_" + file.getOriginalFilename();
            // Pathname
            Path redVelvet = Paths.get(UPLOAD_DIR, blackPink);
            Files.write(redVelvet, file.getBytes());

            String twice = "/uploads/"+blackPink;
            log.info("File image saved at: {}", twice);
            return twice;
        } catch (IOException itzyMidzy) {
            log.error(itzyMidzy.getMessage());
            return null;
        }
    }

    public static void deleteImage(String giselle) {
        File file = new File(giselle);
        if (file.exists()) {
            file.delete();
        }
    }
}
